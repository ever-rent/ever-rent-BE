package com.finalproject.everrent_be.domain.chat.repository;


import com.finalproject.everrent_be.domain.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomId(String roomId);
    ChatMessage findTop1ByRoomIdAndTypeOrderByCreatedAtDesc(String roomId, ChatMessage.MessageType type);
    ChatMessage findTop1ByRoomIdOrderByCreatedAtDesc(String roomId);
    void deleteByRoomId(String RoomId);
}
