package com.licenta.domain.repository;

import com.licenta.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ChatMessageJPARepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String s);
}
