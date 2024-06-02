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
    private final ProfilePictureService profilePictureService;

    public ChatRoomServiceImpl(ChatRoomJPARepository chatRoomJPARepository, UserService userService, ChatRoomVOMapper chatRoomVOMapper, ProfilePictureService profilePictureService) {
        this.chatRoomJPARepository = chatRoomJPARepository;
        this.userService = userService;
        this.chatRoomVOMapper = chatRoomVOMapper;
        this.profilePictureService = profilePictureService;
    }

    @Override
    public Optional<String> getId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomJPARepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or( () -> {
                    if(createNewRoomIfNotExists) {
                        String chatId = createChatIdAndSaveChatRooms(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatIdAndSaveChatRooms(Long senderId, Long recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = save(senderId, recipientId, chatId);
        chatRoomJPARepository.save(senderRecipient);

        ChatRoom recipientSender = save(recipientId, senderId, chatId);
        chatRoomJPARepository.save(recipientSender);

        return chatId;
    }

    private ChatRoom save(Long senderId, Long recipientId, String chatId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatId(chatId);
        chatRoom.setSender(userService.findById(senderId));
        chatRoom.setRecipient(userService.findById(recipientId));
        return chatRoom;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomVO> findMyChatRooms() {
        Long loggedInUserId = UserContextHolder.getUserContext().getUserId();
        return chatRoomJPARepository.findAllBySender_Id(loggedInUserId)
                .stream()
                .map(chatRoom -> chatRoomVOMapper.getVOFromEntity(chatRoom,
                        profilePictureService.getBase64EncodedStringOfFileContentByUserId(chatRoom.getRecipient().getId())))
                .toList();
    }
}
