package com.licenta.service.dto;

import com.licenta.domain.ChatMessage;
import com.licenta.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatMessageDTOMapper {
    private final UserService userService;

    public ChatMessageDTOMapper(UserService userService) {
        this.userService = userService;
    }

    public ChatMessage getEntityFromDTO(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatMessageDTO.getChatId());
        chatMessage.setSender(userService.findById(chatMessageDTO.getSenderId()));
        chatMessage.setRecipient(userService.findById(chatMessageDTO.getRecipientId()));
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setRead(false);
        return chatMessage;
    }
}
