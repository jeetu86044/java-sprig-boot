package org.acme.schooltimetabling.solver;

import java.time.Duration;
import java.util.List;

import org.acme.schooltimetabling.domain.Timeslot;
import org.acme.schooltimetabling.persistence.TimeslotRepository;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import org.acme.schooltimetabling.domain.Lesson;
import org.optaplanner.core.api.score.stream.uni.UniConstraintStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeTableConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                roomConflict(constraintFactory),
                teacherConflict(constraintFactory),
                studentGroupConflict(constraintFactory),
                classTeacherFirstLectureMapping(constraintFactory),
                consecutiveContraints(constraintFactory),
                // Soft constraints
                studentRoomStability(constraintFactory),
                teacherTimeEfficiency(constraintFactory),
                studentGroupSubjectVariety(constraintFactory)
        };
    }

    private Constraint classTeacherFirstLectureMapping(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal(lesson -> lesson.getTimeslot().getDayOfWeek())

                )
                .filter((lesson, lesson2) -> {
                    return ((lesson.isClassTeacher() && !lesson2.isClassTeacher()) && lesson.getTimeslot().getStartTime().isAfter(lesson2.getTimeslot().getStartTime()))
                            ||
                            ((lesson2.isClassTeacher() && !lesson.isClassTeacher()) && lesson2.getTimeslot().getStartTime().isAfter(lesson.getTimeslot().getStartTime()));
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Not a first lecture");
    }

    private Constraint consecutiveContraints(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroup)
                )
                .filter((lesson, lesson2) -> {
                   return  (lesson.getSubject().equals("Games") && lesson2.getSubject().equals("Games")) &&

                           (     (lesson.getTimeslot().getDayOfWeek() != lesson2.getTimeslot().getDayOfWeek()) ||

                           !( lesson.getTimeslot().getEndTime().equals(lesson2.getTimeslot().getStartTime())
                            || lesson2.getTimeslot().getEndTime().equals(lesson.getTimeslot().getStartTime()))) ;
//                    return ((lesson.isClassTeacher() && !lesson2.isClassTeacher()) && lesson.getTimeslot().getStartTime().isAfter(lesson2.getTimeslot().getStartTime()))
//                            ||
//                            ((lesson2.isClassTeacher() && !lesson.isClassTeacher()) && lesson2.getTimeslot().getStartTime().isAfter(lesson.getTimeslot().getStartTime()));
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Games period constraint");
    }


    Constraint roomConflict(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                // Select each pair of 2 different lessons ...
                .forEachUniquePair(Lesson.class,
                        // ... in the same timeslot ...
                        Joiners.equal(Lesson::getTimeslot),
                        // ... in the same room ...
                        Joiners.equal(Lesson::getRoom))
                // ... and penalize each pair with a hard weight.
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Room conflict");
    }

//    Constraint roomConflict(ConstraintFactory constraintFactory) {
//        // A room can accommodate at most one lesson at the same time.
//        return constraintFactory
//                // Select each pair of 2 different lessons ...
//                .forEachUniquePair(Lesson.class,
//                        // ... in the same timeslot ...
//                        Joiners.equal(Lesson::getTimeslot),
//                        // ... in the same room ...
//                        Joiners.equal(Lesson::getRoom))
//                // ... and penalize each pair with a hard weight.
//                .penalize(HardSoftScore.ONE_HARD)
//                .asConstraint("Room conflict");
//    }

    Constraint teacherConflict(ConstraintFactory constraintFactory) {
        // A teacher can teach at most one lesson at the same time.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getTeacher))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        // A student can attend at most one lesson at the same time.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getStudentGroup))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

    Constraint studentRoomStability(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach in a single room.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getStudentGroup))
                .filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom())
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher room stability");
    }

    Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach sequential lessons and dislikes gaps between lessons.
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class, Joiners.equal(Lesson::getTeacher),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }

    Constraint studentGroupSubjectVariety(ConstraintFactory constraintFactory) {
        // A student group dislikes sequential lessons on the same subject.
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getSubject),
                        Joiners.equal(Lesson::getStudentGroup))
                       // Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                   // return !between.isNegative() && between.compareTo(Duration.ofHours(8)) <= 0;
                    return !lesson1.equals(lesson2) && lesson1.getTimeslot().getDayOfWeek() == lesson2.getTimeslot().getDayOfWeek();
                })
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Student group subject variety");
    }

}
