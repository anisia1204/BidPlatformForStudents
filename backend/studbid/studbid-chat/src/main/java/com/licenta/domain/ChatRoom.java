package com.licenta.domain;

import javax.persistence.*;

@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id")
    private String chatId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "FK_CHAT_ROOM__SENDER"))
    private User sender;
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", foreignKey = @ForeignKey(name = "FK_CHAT_ROOM__RECIPIENT"))
    private User recipient;

    public ChatRoom() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
