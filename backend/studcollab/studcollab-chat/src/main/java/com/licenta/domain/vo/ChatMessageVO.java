package com.licenta.domain.vo;

import java.time.LocalDateTime;

public class ChatMessageVO {
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessageVO(Long id, String chatId, Long senderId, Long recipientId, String content, LocalDateTime timestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
