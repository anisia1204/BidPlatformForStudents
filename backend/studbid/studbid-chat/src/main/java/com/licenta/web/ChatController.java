package com.licenta.web;

import com.licenta.domain.ChatMessage;
import com.licenta.service.ChatMessageService;
import com.licenta.service.dto.ChatMessageDTO;
import com.licenta.service.dto.ChatNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
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

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable Long senderId, @PathVariable Long recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
