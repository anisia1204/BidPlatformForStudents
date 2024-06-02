package com.licenta.service.email;

public interface EmailSender {
    void send(String to, String name, String link, String subject);
}
