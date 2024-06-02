package com.licenta.service.dto;

import com.licenta.domain.TutoringType;

import java.time.LocalDate;

public class TutoringServiceDTO extends AnnouncementDTO{
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hoursPerSession;
    private TutoringType tutoringType;

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
