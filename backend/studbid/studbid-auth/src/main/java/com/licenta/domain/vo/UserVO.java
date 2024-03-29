package com.licenta.domain.vo;

import java.time.LocalDateTime;

public class UserVO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Double points;
    private LocalDateTime createdAt;

    public UserVO(Long id, String firstName, String lastName, String email, Double points, LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = points;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Double getPoints() {
        return points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
