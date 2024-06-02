package com.licenta.domain.vo;

import com.licenta.domain.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageVOMapper {
    public ChatMessageVO getVOFromEntity(ChatMessage chatMessage) {
        return new ChatMessageVO(
                chatMessage.getId(),
                chatMessage.getChatId(),
                chatMessage.getSender().getId(),
                chatMessage.getRecipient().getId(),
                chatMessage.getContent(),
                chatMessage.getTimestamp()
        );
    }
}
