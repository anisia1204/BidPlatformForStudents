package com.licenta.domain.vo;

public class ChatRoomListItemVO {
    private ChatRoomVO chatRoomVO;
    private ChatMessageVO lastMessage;
    private Boolean hasUnreadMessages;

    public ChatRoomListItemVO(ChatRoomVO chatRoomVO, ChatMessageVO lastMessage, Boolean hasUnreadMessages) {
        this.chatRoomVO = chatRoomVO;
        this.lastMessage = lastMessage;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public ChatRoomVO getChatRoomVO() {
        return chatRoomVO;
    }

    public ChatMessageVO getLastMessage() {
        return lastMessage;
    }

    public Boolean getHasUnreadMessages() {
        return hasUnreadMessages;
    }
}
