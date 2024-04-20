package com.licenta.domain.vo;

public class UserDetailsVO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String base64EncodedStringOfFileContent;

    public UserDetailsVO(Long id, String firstName, String lastName, String email, String base64EncodedStringOfFileContent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.base64EncodedStringOfFileContent = base64EncodedStringOfFileContent;
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

    public String getBase64EncodedStringOfFileContent() {
        return base64EncodedStringOfFileContent;
    }
}
