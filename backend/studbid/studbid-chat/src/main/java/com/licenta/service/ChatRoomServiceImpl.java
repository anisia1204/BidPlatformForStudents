package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.ChatRoom;
import com.licenta.domain.repository.ChatRoomJPARepository;
import com.licenta.domain.vo.ChatRoomVO;
import com.licenta.domain.vo.ChatRoomVOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomJPARepository chatRoomJPARepository;
    private final UserService userService;
    private final ChatRoomVOMapper chatRoomVOMapper;

    public ChatRoomServiceImpl(ChatRoomJPARepository chatRoomJPARepository, UserService userService, ChatRoomVOMapper chatRoomVOMapper) {
        this.chatRoomJPARepository = chatRoomJPARepository;
        this.userService = userService;
        this.chatRoomVOMapper = chatRoomVOMapper;
    }

    @Override
    public Optional<String> getId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomJPARepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or( () -> {
                    if(createNewRoomIfNotExists) {
                        String chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(Long senderId, Long recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setSender(userService.findById(senderId));
        senderRecipient.setRecipient(userService.findById(recipientId));

        ChatRoom recipientSender = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setSender(userService.findById(recipientId));
        senderRecipient.setRecipient(userService.findById(senderId));

        chatRoomJPARepository.save(senderRecipient);
        chatRoomJPARepository.save(recipientSender);

        return chatId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomVO> findMyChatRooms() {
        return chatRoomJPARepository.findAllBySender_Id(UserContextHolder.getUserContext().getUserId())
                .stream()
                .map(chatRoomVOMapper::getVOFromEntity)
                .toList();
    }
}
