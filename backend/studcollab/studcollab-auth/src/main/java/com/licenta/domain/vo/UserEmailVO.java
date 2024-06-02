package com.licenta.domain.vo;

public class UserEmailVO {
    private Long id;
    private String email;

    public UserEmailVO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
