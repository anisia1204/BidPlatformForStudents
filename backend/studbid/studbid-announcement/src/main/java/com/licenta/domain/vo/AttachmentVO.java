package com.licenta.domain.vo;

import java.util.Base64;

public class AttachmentVO {
    private Long id;
    private String base64EncodedStringOfFileContent;

    public AttachmentVO(Long id, String base64EncodedStringOfFileContent) {
        this.id = id;
        this.base64EncodedStringOfFileContent = base64EncodedStringOfFileContent;
    }

    public Long getId() {
        return id;
    }

    public String getBase64EncodedStringOfFileContent() {
        return base64EncodedStringOfFileContent;
    }
}
