package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.ChatMessage;
import com.licenta.domain.repository.ChatMessageJPARepository;
import com.licenta.domain.vo.ChatMessageVO;
import com.licenta.domain.vo.ChatMessageVOMapper;
import com.licenta.domain.vo.ChatRoomVO;
import com.licenta.domain.vo.ChatRoomListItemVO;
import com.licenta.service.dto.ChatMessageDTO;
import com.licenta.service.dto.ChatMessageDTOMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class ChatMessageServiceImpl implements ChatMessageService{
    private final ChatMessageJPARepository chatMessageJPARepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageVOMapper chatMessageVOMapper;
    private final ChatMessageDTOMapper chatMessageDTOMapper;

    public ChatMessageServiceImpl(ChatMessageJPARepository chatMessageJPARepository, ChatRoomService chatRoomService, ChatMessageVOMapper chatMessageVOMapper, ChatMessageDTOMapper chatMessageDTOMapper) {
        this.chatMessageJPARepository = chatMessageJPARepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageVOMapper = chatMessageVOMapper;
        this.chatMessageDTOMapper = chatMessageDTOMapper;
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
        ChatMessage chatMessage = chatMessageDTOMapper.getEntityFromDTO(chatMessageDTO);
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

    @Transactional(readOnly = true)
    public List<ChatRoomListItemVO> getChatRoomListItemVOsSortedDescendingByLastMessageTimestamp(List<ChatRoomVO> chatRooms) {
        List<ChatRoomListItemVO> chatRoomListItemVOS = new ArrayList<>();
        chatRooms.forEach(chatRoomVO -> {
            ChatMessage chatMessage = findLastMessageOfChatRoom(chatRoomVO.getChatId());
            ChatMessageVO chatMessageVO = chatMessageVOMapper.getVOFromEntity(chatMessage);
            Boolean hasUnreadMessages = !chatMessageVO.getIsRead() &&
                    !Objects.equals(chatMessageVO.getSenderId(), UserContextHolder.getUserContext().getUserId());
            chatRoomListItemVOS.add(new ChatRoomListItemVO(
                    chatRoomVO,
                    chatMessageVO,
                    hasUnreadMessages));
        });
        return sortChatRoomListItemVOsDescendingByLastMessageTimestamp(chatRoomListItemVOS);
    }

    private static List<ChatRoomListItemVO> sortChatRoomListItemVOsDescendingByLastMessageTimestamp(List<ChatRoomListItemVO> chatRoomListItemVOS) {
        return chatRoomListItemVOS
                .stream()
                .sorted(Comparator.comparing((ChatRoomListItemVO vo) -> vo.getLastMessage().getTimestamp()).reversed())
                .toList();
    }

    private ChatMessage findLastMessageOfChatRoom(String chatId) {
        return chatMessageJPARepository.findFirstByChatIdOrderByTimestampDesc(chatId);
    }

    @Override
    @Transactional
    public void markUnreadChatMessagesOfChatRoomAsRead(ChatRoomListItemVO chatRoomListItemVO) {
        String chat_id = chatRoomListItemVO.getChatRoomVO().getChatId();
        chatMessageJPARepository.findAllByChatIdAndIsReadIsFalse(chat_id)
                .forEach(this::markChatMessageAsRead);
    }

    private void markChatMessageAsRead(ChatMessage unreadMessage) {
        unreadMessage.setRead(true);
        chatMessageJPARepository.save(unreadMessage);
    }


    @Override
    @Transactional(readOnly = true)
    public Long getCountOfUnreadMessagesOfUser() {
        return chatMessageJPARepository.countAllByRecipientIdAndIsReadIsFalse(UserContextHolder.getUserContext().getUserId());
    }
}
