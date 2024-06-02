package com.licenta.web;

import com.licenta.domain.ChatMessage;
import com.licenta.domain.vo.ChatMessageVO;
import com.licenta.domain.vo.ChatRoomVO;
import com.licenta.domain.vo.ChatRoomListItemVO;
import com.licenta.service.ChatMessageService;
import com.licenta.service.ChatRoomService;
import com.licenta.service.dto.ChatMessageDTO;
import com.licenta.service.dto.ChatNotificationDTO;
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
                new ChatNotificationDTO(
                        savedMessage.getChatId(),
                        savedMessage.getSender().getId(),
                        savedMessage.getRecipient().getId(),
                        savedMessage.getContent(),
                        savedMessage.getTimestamp().toString()
                )
        );
    }

    @GetMapping("/messages/{recipientId}")
    public ResponseEntity<List<ChatMessageVO>> findChatMessages(@PathVariable Long recipientId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(recipientId, page, size));
    }

    @GetMapping("/chat-rooms")
    public ResponseEntity<List<ChatRoomListItemVO>> findMyChatRooms() {
        List<ChatRoomVO> chatRooms = chatRoomService.findMyChatRooms();
        List<ChatRoomListItemVO> chatRoomListItemVOS = chatMessageService.getChatRoomListItemVOsSortedDescendingByLastMessageTimestamp(chatRooms);
        return ResponseEntity.ok(chatRoomListItemVOS);
    }
}
