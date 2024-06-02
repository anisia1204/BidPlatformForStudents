package com.licenta.domain.vo;

public class ChatRoomVO {
    private Long id;
    private String chatId;
    private Long recipientId;
    private String firstName;
    private String lastName;
    private String base64EncodedStringOfFileContent;

    public ChatRoomVO(Long id, String chatId, Long recipientId, String firstName, String lastName, String base64EncodedStringOfFileContent) {
        this.id = id;
        this.chatId = chatId;
        this.recipientId = recipientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.base64EncodedStringOfFileContent = base64EncodedStringOfFileContent;
    }

    public Long getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBase64EncodedStringOfFileContent() {
        return base64EncodedStringOfFileContent;
    }
}
