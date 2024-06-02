package com.licenta.domain.vo;

public class ProfilePictureVO {
    private Long id;
    private String base64EncodedStringOfFileContent;

    public ProfilePictureVO(Long id, String base64EncodedStringOfFileContent) {
        this.id = id;
        this.base64EncodedStringOfFileContent = base64EncodedStringOfFileContent;
    }

    public ProfilePictureVO() {
    }

    public Long getId() {
        return id;
    }

    public String getBase64EncodedStringOfFileContent() {
        return base64EncodedStringOfFileContent;
    }
}
