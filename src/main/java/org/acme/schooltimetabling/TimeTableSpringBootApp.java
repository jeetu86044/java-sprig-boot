package org.acme.schooltimetabling;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Room;
import org.acme.schooltimetabling.domain.Timeslot;
import org.acme.schooltimetabling.persistence.LessonRepository;
import org.acme.schooltimetabling.persistence.RoomRepository;
import org.acme.schooltimetabling.persistence.TimeslotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootApplication
public class TimeTableSpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(TimeTableSpringBootApp.class, args);
    }

    @Value("${timeTable.demoData:NONE}")
    private DemoData demoData;

    @Bean
    public CommandLineRunner demoData(
            TimeslotRepository timeslotRepository,
            RoomRepository roomRepository,
            LessonRepository lessonRepository) {
        return (args) -> {
            if (demoData == DemoData.NONE) {
                addTimeSlots(timeslotRepository);
                addLessons(lessonRepository);
                addRooms(roomRepository);
                return;
            }

            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
            timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
            if (demoData == DemoData.LARGE) {
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(8, 30), LocalTime.of(9, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(9, 30), LocalTime.of(10, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(10, 30), LocalTime.of(11, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(13, 30), LocalTime.of(14, 30)));
                timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(14, 30), LocalTime.of(15, 30)));
            }

            roomRepository.save(new Room("Room A"));
            roomRepository.save(new Room("Room B"));
            roomRepository.save(new Room("Room C"));
            if (demoData == DemoData.LARGE) {
                roomRepository.save(new Room("Room D"));
                roomRepository.save(new Room("Room E"));
                roomRepository.save(new Room("Room F"));
            }

            lessonRepository.save(new Lesson("Math", "A. Turing", "9th grade", false));
            lessonRepository.save(new Lesson("Math", "A. Turing", "9th grade", false));
            lessonRepository.save(new Lesson("Physics", "M. Curie", "9th grade", false));
            lessonRepository.save(new Lesson("Chemistry", "M. Curie", "9th grade", false));
            lessonRepository.save(new Lesson("Biology", "C. Darwin", "9th grade", false));
            lessonRepository.save(new Lesson("History", "I. Jones", "9th grade", false));
            lessonRepository.save(new Lesson("English", "I. Jones", "9th grade", false));
            lessonRepository.save(new Lesson("English", "I. Jones", "9th grade", false));
            lessonRepository.save(new Lesson("Spanish", "P. Cruz", "9th grade", false));
            lessonRepository.save(new Lesson("Spanish", "P. Cruz", "9th grade", false));
            if (demoData == DemoData.LARGE) {
                lessonRepository.save(new Lesson("Math", "A. Turing", "9th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "9th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "9th grade", false));
                lessonRepository.save(new Lesson("ICT", "A. Turing", "9th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "9th grade", false));
                lessonRepository.save(new Lesson("Geography", "C. Darwin", "9th grade", false));
                lessonRepository.save(new Lesson("Geology", "C. Darwin", "9th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "9th grade", false));
                lessonRepository.save(new Lesson("English", "I. Jones", "9th grade", false));
                lessonRepository.save(new Lesson("Drama", "I. Jones", "9th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "9th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "9th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "9th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "9th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "9th grade", false));
            }

            lessonRepository.save(new Lesson("Math", "A. Turing", "10th grade", false));
            lessonRepository.save(new Lesson("Math", "A. Turing", "10th grade", false));
            lessonRepository.save(new Lesson("Math", "A. Turing", "10th grade", false));
            lessonRepository.save(new Lesson("Physics", "M. Curie", "10th grade", false));
            lessonRepository.save(new Lesson("Chemistry", "M. Curie", "10th grade", false));
            lessonRepository.save(new Lesson("French", "M. Curie", "10th grade", false));
            lessonRepository.save(new Lesson("Geography", "C. Darwin", "10th grade", false));
            lessonRepository.save(new Lesson("History", "I. Jones", "10th grade", false));
            lessonRepository.save(new Lesson("English", "P. Cruz", "10th grade", false));
            lessonRepository.save(new Lesson("Spanish", "P. Cruz", "10th grade", false));
            if (demoData == DemoData.LARGE) {
                lessonRepository.save(new Lesson("Math", "A. Turing", "10th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "10th grade", false));
                lessonRepository.save(new Lesson("ICT", "A. Turing", "10th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "10th grade", false));
                lessonRepository.save(new Lesson("Biology", "C. Darwin", "10th grade", false));
                lessonRepository.save(new Lesson("Geology", "C. Darwin", "10th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "10th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "10th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "10th grade", false));
                lessonRepository.save(new Lesson("Drama", "I. Jones", "10th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "10th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "10th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "10th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "10th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "10th grade", false));

                lessonRepository.save(new Lesson("Math", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("ICT", "A. Turing", "11th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "11th grade", false));
                lessonRepository.save(new Lesson("Chemistry", "M. Curie", "11th grade", false));
                lessonRepository.save(new Lesson("French", "M. Curie", "11th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "11th grade", false));
                lessonRepository.save(new Lesson("Geography", "C. Darwin", "11th grade", false));
                lessonRepository.save(new Lesson("Biology", "C. Darwin", "11th grade", false));
                lessonRepository.save(new Lesson("Geology", "C. Darwin", "11th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "11th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "11th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "11th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "11th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "11th grade", false));
                lessonRepository.save(new Lesson("Spanish", "P. Cruz", "11th grade", false));
                lessonRepository.save(new Lesson("Drama", "P. Cruz", "11th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "11th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "11th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "11th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "11th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "11th grade", false));

                lessonRepository.save(new Lesson("Math", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("Math", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("ICT", "A. Turing", "12th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "12th grade", false));
                lessonRepository.save(new Lesson("Chemistry", "M. Curie", "12th grade", false));
                lessonRepository.save(new Lesson("French", "M. Curie", "12th grade", false));
                lessonRepository.save(new Lesson("Physics", "M. Curie", "12th grade", false));
                lessonRepository.save(new Lesson("Geography", "C. Darwin", "12th grade", false));
                lessonRepository.save(new Lesson("Biology", "C. Darwin", "12th grade", false));
                lessonRepository.save(new Lesson("Geology", "C. Darwin", "12th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "12th grade", false));
                lessonRepository.save(new Lesson("History", "I. Jones", "12th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "12th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "12th grade", false));
                lessonRepository.save(new Lesson("English", "P. Cruz", "12th grade", false));
                lessonRepository.save(new Lesson("Spanish", "P. Cruz", "12th grade", false));
                lessonRepository.save(new Lesson("Drama", "P. Cruz", "12th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "12th grade", false));
                lessonRepository.save(new Lesson("Art", "S. Dali", "12th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "12th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "12th grade", false));
                lessonRepository.save(new Lesson("Physical education", "C. Lewis", "12th grade", false));
            }

            Lesson lesson = lessonRepository.findAll(Sort.by("id")).iterator().next();
            lesson.setTimeslot(timeslotRepository.findAll(Sort.by("id")).iterator().next());
            lesson.setRoom(roomRepository.findAll(Sort.by("id")).iterator().next());

            lessonRepository.save(lesson);
        };
    }

    private void addRooms(RoomRepository roomRepository) {
        roomRepository.save(new Room("1A-ROOM"));
        roomRepository.save(new Room("1B-ROOM"));
        roomRepository.save(new Room("2A-ROOM"));
        roomRepository.save(new Room("2B-ROOM"));
        roomRepository.save(new Room("3A-ROOM"));
        roomRepository.save(new Room("3B-ROOM"));
        roomRepository.save(new Room("4A-ROOM"));
        roomRepository.save(new Room("4B-ROOM"));
        roomRepository.save(new Room("5A-ROOM"));
        roomRepository.save(new Room("5B-ROOM"));
        roomRepository.save(new Room("6A-ROOM"));
        roomRepository.save(new Room("6B-ROOM"));
        roomRepository.save(new Room("7A-ROOM"));
        roomRepository.save(new Room("7B-ROOM"));
        roomRepository.save(new Room("8A-ROOM"));
        roomRepository.save(new Room("8B-ROOM"));
        roomRepository.save(new Room("9A-ROOM"));
        roomRepository.save(new Room("9B-ROOM"));
        roomRepository.save(new Room("10-ROOM"));
        roomRepository.save(new Room("11-ROOM"));
        roomRepository.save(new Room("12-ROOM"));
    }

    private void addLessons(LessonRepository lessonRepository) {
        List<Lectures> lectures = new ArrayList<>();
        lectures.add(new Lectures("Surabhi","Hindi",  7, "1A", true));
        lectures.add(new Lectures("Surabhi", "Hindi", 7, "1B", false));

        lectures.add(new Lectures("Ruchi", "Hindi", 7, "2A", true));
        lectures.add(new Lectures("Ruchi", "Hindi", 7, "2B", false));

        lectures.add(new Lectures("Jyoti", "Hindi", 7, "3A", true));
        lectures.add(new Lectures("Jyoti", "Hindi", 7, "3B", false));

        lectures.add(new Lectures("Sarika", "Hindi", 7, "4A", true));
        lectures.add(new Lectures("Sarika", "Hindi", 7, "6A", false));
        lectures.add(new Lectures("Sarika", "Hindi", 7, "6B", false));
        lectures.add(new Lectures("Sarika", "Hindi", 7, "8A", false));
        lectures.add(new Lectures("Sarika", "Hindi", 7, "8B", false));

        lectures.add(new Lectures("Archana Dixit", "Hindi", 7, "4B", true));
        lectures.add(new Lectures("Archana Dixit", "Hindi", 7, "5A", false));
        lectures.add(new Lectures("Archana Dixit", "Hindi", 7, "5B", false));
        lectures.add(new Lectures("Archana Dixit", "Hindi", 7, "7A", false));
        lectures.add(new Lectures("Archana Dixit", "Hindi", 7, "7B", false));

        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "5A", true));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "5B", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "6A", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "6B", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "7A", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "7B", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "8A", false));
        lectures.add(new Lectures("Jyoti 2", "Sanskrit", 2, "8B", false));

        lectures.add(new Lectures("Bhavneet", "English", 7, "2A", false));
        lectures.add(new Lectures("Bhavneet", "English", 7, "2B", true));
        lectures.add(new Lectures("Bhavneet", "English", 7, "4A", false));
        lectures.add(new Lectures("Bhavneet", "English", 7, "4B", false));

        lectures.add(new Lectures("Nancy", "English", 7, "3A", false));
        lectures.add(new Lectures("Nancy", "English", 7, "3B", true));

        lectures.add(new Lectures("Amulaya", "English", 7, "5A", false));
        lectures.add(new Lectures("Amulaya", "English", 7, "5B", true));

        lectures.add(new Lectures("Mini", "English", 7, "6A", true));
        lectures.add(new Lectures("Mini", "English", 7, "6B", false));
        lectures.add(new Lectures("Mini", "English", 7, "7A", false));
        lectures.add(new Lectures("Mini", "English", 7, "7B", false));

        lectures.add(new Lectures("Swati", "English", 7, "8A", true));
        lectures.add(new Lectures("Swati", "English", 7, "8B", false));
        lectures.add(new Lectures("Swati", "English", 7, "10", false));
        lectures.add(new Lectures("Swati", "English", 7, "11", false));

        lectures.add(new Lectures("Rajshree", "English", 7, "9A", true));
        lectures.add(new Lectures("Rajshree", "English", 7, "9B", false));
        lectures.add(new Lectures("Rajshree", "English", 7, "12", false));

        lectures.add(new Lectures("Deepali", "Sst", 7, "9A", false));
        lectures.add(new Lectures("Deepali", "Sst", 7, "9B", true));
        lectures.add(new Lectures("Deepali", "Sst", 7, "10", false));

        lectures.add(new Lectures("Deepa lal", "Sst", 7, "5A", false));
        lectures.add(new Lectures("Deepa lal", "Sst", 7, "5B", true));
        lectures.add(new Lectures("Deepa lal", "Sst", 7, "7A", false));
        lectures.add(new Lectures("Deepa lal", "Sst", 7, "7B", false));

        lectures.add(new Lectures("Rajshree Sst", "Sst", 7, "8A", false));
        lectures.add(new Lectures("Rajshree Sst", "Sst", 7, "8B", true));

        lectures.add(new Lectures("Amulaya Sst", "Sst", 7, "4A", false));
        lectures.add(new Lectures("Amulaya Sst", "Sst", 7, "4B", false));
        lectures.add(new Lectures("Amulaya Sst", "Sst", 7, "6A", false));

        lectures.add(new Lectures("Swati Sst", "Sst", 7, "6B", true));

        lectures.add(new Lectures("Harleen", "E.V.S", 7, "1A", false));
        lectures.add(new Lectures("Harleen", "E.V.S", 7, "1B", false));

        lectures.add(new Lectures("Nancy", "E.V.S", 7, "3A", false));
        lectures.add(new Lectures("Nancy", "E.V.S", 7, "3B", false));

        for(Lectures lectures1: lectures){
            for(int i=0; i < lectures1.numberOfPeriodsInAWeek; i++){
                lessonRepository.save(new Lesson(lectures1.subject, lectures1.teacherName, lectures1.claasName, lectures1.isClassTeacher));
            }
        }

    }

    public static class Lectures {
        private String teacherName;
        private int numberOfPeriodsInAWeek;
        private String claasName;

        private String subject;

        private boolean isClassTeacher;

        public Lectures(String teacherName,String subject, int numberOfPeriodsInAWeek, String claasName, boolean isClassTeacher) {
            this.teacherName = teacherName;
            this.subject = subject;
            this.numberOfPeriodsInAWeek = numberOfPeriodsInAWeek;
            this.claasName = claasName;
            this.isClassTeacher = isClassTeacher;
        }
    }

    private void addTimeSlots(TimeslotRepository timeslotRepository) {
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.MONDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));

        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.TUESDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));

        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.WEDNESDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));


        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.THURSDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));

        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.FRIDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));

        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(8, 30), LocalTime.of(9, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(9, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(9, 30), LocalTime.of(10, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(10, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(10, 30), LocalTime.of(11, 0)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(11, 0), LocalTime.of(11, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(13, 0), LocalTime.of(13, 30)));
        timeslotRepository.save(new Timeslot(DayOfWeek.SATURDAY, LocalTime.of(13, 30), LocalTime.of(14, 0)));
    }

    public enum DemoData {
        NONE,
        SMALL,
        LARGE
    }

}
