package com.licenta.domain.vo;

import com.licenta.domain.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomVOMapper {
    public ChatRoomVO getVOFromEntity(ChatRoom chatRoom, String base64EncodedStringOfFileContent) {
        return new ChatRoomVO(
                chatRoom.getId(),
                chatRoom.getChatId(),
                chatRoom.getRecipient().getId(),
                chatRoom.getRecipient().getFirstName(),
                chatRoom.getRecipient().getLastName(),
                base64EncodedStringOfFileContent);
    }
}
