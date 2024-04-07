package com.licenta.service;

import com.licenta.domain.ChatMessage;
import com.licenta.domain.vo.ChatMessageVO;
import com.licenta.service.dto.ChatMessageDTO;

import java.util.List;

public interface ChatMessageService {
    ChatMessage save(ChatMessageDTO chatMessageDTO);

    List<ChatMessageVO> findChatMessages(Long recipientId, int page, int size);
}
