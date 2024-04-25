package com.licenta.domain.vo;

public class QRCodeVO {
    private Long id;
    private String base64EncodedStringOfQRCode;

    public QRCodeVO(Long id, String base64EncodedStringOfQRCode) {
        this.id = id;
        this.base64EncodedStringOfQRCode = base64EncodedStringOfQRCode;
    }

    public Long getId() {
        return id;
    }

    public String getBase64EncodedStringOfQRCode() {
        return base64EncodedStringOfQRCode;
    }
}
