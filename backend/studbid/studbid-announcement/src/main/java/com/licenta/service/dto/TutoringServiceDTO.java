package com.licenta.service.dto;

import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.TutoringType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TutoringServiceDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Double points;
    private AnnouncementStatus status;
    private LocalDateTime createdAt;
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hoursPerSession;
    private TutoringType tutoringType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public AnnouncementStatus getStatus() {
        return status;
    }

    public void setStatus(AnnouncementStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
