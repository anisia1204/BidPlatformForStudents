package com.licenta.domain.vo;

import java.time.LocalDateTime;

public class WithdrawVO {
    private Long id;
    private String userFullName;
    private Double points;
    private LocalDateTime createdAt;

    public WithdrawVO(Long id, String userFullName, Double points, LocalDateTime createdAt) {
        this.id = id;
        this.userFullName = userFullName;
        this.points = points;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public Double getPoints() {
        return points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
