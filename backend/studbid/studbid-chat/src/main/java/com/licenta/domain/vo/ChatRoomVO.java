package com.licenta.domain.vo;

public class ChatRoomVO {
    private Long id;
    private String chatId;
    private Long recipientId;
    private String name;

    public ChatRoomVO(Long id, String chatId, Long recipientId, String name) {
        this.id = id;
        this.chatId = chatId;
        this.recipientId = recipientId;
        this.name = name;
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

    public String getName() {
        return name;
    }
}
