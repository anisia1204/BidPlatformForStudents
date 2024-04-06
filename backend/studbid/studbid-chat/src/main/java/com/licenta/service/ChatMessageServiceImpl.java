package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.ChatMessage;
import com.licenta.domain.repository.ChatMessageJPARepository;
import com.licenta.service.dto.ChatMessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageJPARepository chatMessageJPARepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public ChatMessageServiceImpl(ChatMessageJPARepository chatMessageJPARepository, ChatRoomService chatRoomService, UserService userService) {
        this.chatMessageJPARepository = chatMessageJPARepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public ChatMessage save(ChatMessageDTO chatMessageDTO) {
        var chatId = chatRoomService.getId(
                chatMessageDTO.getSenderId(),
                chatMessageDTO.getRecipientId(),
                true)
                .orElseThrow();
        chatMessageDTO.setChatId(chatId);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatId(chatId);
        chatMessage.setSender(userService.findById(chatMessageDTO.getSenderId()));
        chatMessage.setRecipient(userService.findById(chatMessageDTO.getRecipientId()));
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setTimestamp(chatMessageDTO.getTimestamp());
        chatMessageJPARepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> findChatMessages(Long recipientId) {
        var chatId = chatRoomService.getId(
                UserContextHolder.getUserContext().getUserId(),
                recipientId,
                false);
        return chatId.map(chatMessageJPARepository::findByChatId).orElse(new ArrayList<>());
    }
}
