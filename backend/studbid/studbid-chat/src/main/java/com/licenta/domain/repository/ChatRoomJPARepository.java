package com.licenta.domain.repository;

import com.licenta.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJPARepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    List<ChatRoom> findAllBySender_Id(Long id);
}
