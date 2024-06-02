package com.licenta.domain.vo;

public class ChatRoomListItemVO {
    private ChatRoomVO chatRoomVO;
    private ChatMessageVO lastMessage;

    public ChatRoomListItemVO(ChatRoomVO chatRoomVO, ChatMessageVO lastMessage) {
        this.chatRoomVO = chatRoomVO;
        this.lastMessage = lastMessage;
    }

    public ChatRoomVO getChatRoomVO() {
        return chatRoomVO;
    }

    public ChatMessageVO getLastMessage() {
        return lastMessage;
    }
}
