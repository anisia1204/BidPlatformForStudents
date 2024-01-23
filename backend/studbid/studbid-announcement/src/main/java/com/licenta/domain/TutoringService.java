package com.licenta.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tutoring_service")
public class TutoringService extends Announcement{

    @Column(name = "subject", nullable = false)
    private String subject;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "hours_per_session")
    private Integer hoursPerSession;
    @Column(name = "tutoring_type", nullable = false)
    private TutoringType tutoringType;
    public TutoringService() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getHoursPerSession() {
        return hoursPerSession;
    }

    public void setHoursPerSession(Integer hoursPerSession) {
        this.hoursPerSession = hoursPerSession;
    }

    public TutoringType getTutoringType() {
        return tutoringType;
    }

    public void setTutoringType(TutoringType tutoringType) {
        this.tutoringType = tutoringType;
    }
}
