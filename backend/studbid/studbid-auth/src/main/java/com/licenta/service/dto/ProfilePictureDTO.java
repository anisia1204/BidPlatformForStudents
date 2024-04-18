package com.licenta.service.dto;

public class ProfilePictureDTO {
    private Long id;
    private Long userId;
    private String name;
    private Long size;
    private byte[] fileContent;
    private String base64EncodedStringOfFileContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getBase64EncodedStringOfFileContent() {
        return base64EncodedStringOfFileContent;
    }

    public void setBase64EncodedStringOfFileContent(String base64EncodedStringOfFileContent) {
        this.base64EncodedStringOfFileContent = base64EncodedStringOfFileContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
