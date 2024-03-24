package com.licenta.service;

import java.util.Optional;

public interface ChatRoomService {
    Optional<String> getId(Long senderId, Long recipientId, boolean createNewRoomIfNotExists);
}
