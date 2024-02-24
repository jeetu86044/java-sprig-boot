package org.acme.schooltimetabling.rest;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.TimeTable;
import org.acme.schooltimetabling.domain.Timeslot;
import org.acme.schooltimetabling.persistence.TimeTableRepository;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.SolutionManager;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/timeTable")
public class TimeTableController {

    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private SolverManager<TimeTable, Long> solverManager;
    @Autowired
    private SolutionManager<TimeTable, HardSoftScore> solutionManager;

    private static final int MAX_TEACHER_ON_PAGE = 7;

    // To try, GET http://localhost:8080/timeTable
    @GetMapping()
    public TimeTable getTimeTable() {
        // Get the solver status before loading the solution
        // to avoid the race condition that the solver terminates between them
        SolverStatus solverStatus = getSolverStatus();
        TimeTable solution = timeTableRepository.findById(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
        solutionManager.update(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        return solution;
    }

    @PostMapping("/solve")
    public void solve() {
        solverManager.solveAndListen(TimeTableRepository.SINGLETON_TIME_TABLE_ID,
                timeTableRepository::findById,
                timeTableRepository::save);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

    @PostMapping("/stopSolving")
    public void stopSolving() {
        solverManager.terminateEarly(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
    }

    @GetMapping("/print")
    public ResponseEntity<Resource> printPdf() throws IOException, DocumentException {

        TimeTable solution = timeTableRepository.findById(TimeTableRepository.SINGLETON_TIME_TABLE_ID);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Map<String, List<Lesson>> teacherToLessionMap = solution.getLessonList().stream()
                .collect(Collectors.groupingBy(Lesson::getTeacher)
        );
        int batchSize = 0;
        List<String> teachers  = new ArrayList<>();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
//        teacherToLessionMap.values().stream().forEach( lessons -> {
//
//            Collections.sort(lessons, new Comparator<Lesson>() {
//                @Override
//                public int compare(Lesson o1, Lesson o2) {
//                    return o1.getTimeslot().getDayOfWeek() > o2.getTimeslot().getDayOfWeek();
//                }
//            })
//                }
//        );

        Map<DayOfWeek, Integer> dayOfWeekIntegerMap = new HashMap<>();
        dayOfWeekIntegerMap.put(DayOfWeek.MONDAY, 1);
        dayOfWeekIntegerMap.put(DayOfWeek.TUESDAY, 2);
        dayOfWeekIntegerMap.put(DayOfWeek.WEDNESDAY, 3);
        dayOfWeekIntegerMap.put(DayOfWeek.THURSDAY, 4);
        dayOfWeekIntegerMap.put(DayOfWeek.FRIDAY, 5);
        dayOfWeekIntegerMap.put(DayOfWeek.SATURDAY, 6);
        dayOfWeekIntegerMap.put(DayOfWeek.SUNDAY, 7);

        List<Timeslot> timeslotList = new ArrayList<>(solution.getTimeslotList());
        timeslotList.sort(new Comparator<Timeslot>() {
            @Override
            public int compare(Timeslot o1, Timeslot o2) {
                Integer day1Value = dayOfWeekIntegerMap.get(o1.getDayOfWeek());
                Integer day2Value = dayOfWeekIntegerMap.get(o2.getDayOfWeek());
                if(day1Value> day2Value)
                    return 1;
                if(day1Value < day2Value)
                    return -1;
                if(o1.getStartTime().isAfter(o2.getStartTime()))
                    return 1;
                return -1;
            }
        });
        
        for(Map.Entry<String, List<Lesson>> teacherToLession : teacherToLessionMap.entrySet()){
            batchSize++;
            teachers.add(teacherToLession.getKey());
            if(batchSize == MAX_TEACHER_ON_PAGE){
                PdfPTable table = new PdfPTable(MAX_TEACHER_ON_PAGE+1);
                int [] relativeWidth = new int[MAX_TEACHER_ON_PAGE+1];
                Arrays.fill(relativeWidth, 1);
                relativeWidth[0] = 2;
                table.setWidths(relativeWidth);
                printTeachers(table, teachers, teacherToLessionMap, timeslotList);
                batchSize = 0;
                teachers = new ArrayList<>();
                document.add(table);
                document.newPage();
            }

        }


//        PdfPTable table = new PdfPTable(3);
//        addTableHeader(table);
//        addRows(table);


//        document.newPage();
//        PdfPTable table2 = new PdfPTable(3);
//        addTableHeader(table2);
//        addRows(table2);



        document.close();
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        //byte[] array = getItFromDB();

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("timeTable.pdf")
                                .build().toString())
                .body(resource);

    }

    private void printTeachers(PdfPTable table, List<String> teachers, Map<String, List<Lesson>> teacherToLessionMap,
                               List<Timeslot> timeslotList) {
        addHeaderRows(table, teachers);
        for(Timeslot timeslot: timeslotList){
            PdfPCell header1 = new PdfPCell();
            header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header1.setBorderWidth(2);
            header1.setPhrase(new Phrase(timeslot.toString()));
            table.addCell(header1);
            for (String teacher: teachers){
                List<Lesson> lessons = teacherToLessionMap.get(teacher);
                Lesson foundLesson = lessons.stream().filter(lesson -> lesson.getTimeslot().equals(timeslot)).findAny().orElse(new Lesson());
                PdfPCell lecture = new PdfPCell();
                lecture.setBackgroundColor(BaseColor.WHITE);
                lecture.setHorizontalAlignment(Element.ALIGN_CENTER);
                lecture.setVerticalAlignment(Element.ALIGN_CENTER);
                lecture.setBorderWidth(1);
                lecture.setPhrase(new Phrase(foundLesson.getStudentGroup()));
                table.addCell(lecture);
            }

        }
    }

    private void addHeaderRows(PdfPTable table, List<String> headerRows) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase("TimeSlots"));
        table.addCell(header);
       headerRows
                .forEach(columnTitle -> {
                    PdfPCell header1 = new PdfPCell();
                    header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header1.setBorderWidth(2);
                    header1.setPhrase(new Phrase(columnTitle));
                    table.addCell(header1);
                });
    }

}
