package com.licenta.service;

import com.licenta.domain.ChatRoom;
import com.licenta.domain.repository.ChatRoomJPARepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService{
    private final ChatRoomJPARepository chatRoomJPARepository;
    private final UserService userService;

    public ChatRoomServiceImpl(ChatRoomJPARepository chatRoomJPARepository, UserService userService) {
        this.chatRoomJPARepository = chatRoomJPARepository;
        this.userService = userService;
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
}
