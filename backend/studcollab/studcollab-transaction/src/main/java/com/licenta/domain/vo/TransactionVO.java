package com.licenta.domain.vo;

import com.licenta.domain.TransactionType;

import java.time.LocalDateTime;
public class TransactionVO {
    private Long id;
    private String announcementTitle;
    private String skill;
    private TransactionType type;
    private Double amount;
    private LocalDateTime createdAt;
    private String secondUserFullName;

    public TransactionVO(Long id, String announcementTitle, String skill, TransactionType type, Double amount, LocalDateTime createdAt, String secondUserFullName) {
        this.id = id;
        this.announcementTitle = announcementTitle;
        this.skill = skill;
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.secondUserFullName = secondUserFullName;
    }

    public Long getId() {
        return id;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public String getSkill() {
        return skill;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSecondUserFullName() {
        return secondUserFullName;
    }
}
