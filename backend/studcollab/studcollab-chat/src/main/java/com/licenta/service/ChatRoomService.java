package com.licenta.service;

import com.licenta.domain.vo.ChatRoomVO;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists);

    List<ChatRoomVO> findMyChatRooms();
}
