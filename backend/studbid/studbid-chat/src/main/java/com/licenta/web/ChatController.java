package com.licenta.web;

import com.licenta.domain.ChatMessage;
import com.licenta.domain.vo.ChatRoomVO;
import com.licenta.service.ChatMessageService;
import com.licenta.service.ChatRoomService;
import com.licenta.service.dto.ChatMessageDTO;
import com.licenta.service.dto.ChatNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessageDTO) {
        ChatMessage savedMessage = chatMessageService.save(chatMessageDTO);
        messagingTemplate.convertAndSendToUser(
                chatMessageDTO.getRecipientId().toString(),
                "/queue/messages",
                new ChatNotification(
                        savedMessage.getChatId(),
                        savedMessage.getSender().getId(),
                        savedMessage.getRecipient().getId(),
                        savedMessage.getContent()
                )
        );
    }

    @GetMapping("/messages/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(recipientId));
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity<List<ChatRoomVO>> findMyChatRooms() {
        return ResponseEntity.ok(chatRoomService.findMyChatRooms());
    }
}
