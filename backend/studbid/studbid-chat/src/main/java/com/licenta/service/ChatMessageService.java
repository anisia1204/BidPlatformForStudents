package com.licenta.service;

import com.licenta.domain.ChatMessage;
import com.licenta.service.dto.ChatMessageDTO;

import java.util.List;

public interface ChatMessageService {
    ChatMessage save(ChatMessageDTO chatMessageDTO);

    List<ChatMessage> findChatMessages(Long senderId, Long recipientId);
}
