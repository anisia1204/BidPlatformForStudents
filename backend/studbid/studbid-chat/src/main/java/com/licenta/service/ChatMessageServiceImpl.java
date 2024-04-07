package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.ChatMessage;
import com.licenta.domain.repository.ChatMessageJPARepository;
import com.licenta.domain.vo.ChatMessageVO;
import com.licenta.domain.vo.ChatMessageVOMapper;
import com.licenta.service.dto.ChatMessageDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageJPARepository chatMessageJPARepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageVOMapper chatMessageVOMapper;

    public ChatMessageServiceImpl(ChatMessageJPARepository chatMessageJPARepository, ChatRoomService chatRoomService, UserService userService, ChatMessageVOMapper chatMessageVOMapper) {
        this.chatMessageJPARepository = chatMessageJPARepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.chatMessageVOMapper = chatMessageVOMapper;
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
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageJPARepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessageVO> findChatMessages(Long recipientId, int page, int size) {
        var chatId = chatRoomService.getId(
                UserContextHolder.getUserContext().getUserId(),
                recipientId,
                false);

        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());

        return chatMessageJPARepository.findByChatId(chatId.get(), pageable)
                .stream()
                .map(chatMessageVOMapper::getVOFromEntity)
                .toList();
    }
}
