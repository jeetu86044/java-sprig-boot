package org.acme.schooltimetabling.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.optaplanner.core.api.domain.lookup.PlanningId;

@Entity
public class Timeslot {

    @PlanningId
    @Id @GeneratedValue
    private Long id;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    // No-arg constructor required for Hibernate
    public Timeslot() {
    }

    public Timeslot(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Timeslot(long id, DayOfWeek dayOfWeek, LocalTime startTime) {
        this(dayOfWeek, startTime, startTime.plusMinutes(50));
        this.id = id;
    }

    @Override
    public String toString() {
        return dayOfWeek.name().substring(0, 3) + " " + startTime + ":" + endTime;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public boolean equals(Timeslot obj) {
        return obj.dayOfWeek == this.dayOfWeek && obj.getStartTime().equals(this.getStartTime()) && obj.getEndTime().equals(this.endTime);
    }
    public Long getId() {
        return id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}
