package com.finalproject.everrent_be.domain.chat.repository;

import com.finalproject.everrent_be.domain.chat.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByRoomDetail_Member_MemberIdOrderByCreatedAtDesc(Long memberId);
    List<Chat> findByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);
    Optional<Chat> findFirstByRoomDetail_RoomInfo_IdOrderByCreatedAtDesc(Long roomInfoId);

    List<Chat>findByRoomDetail_RoomInfo_IdOrderByCreatedAtAsc(Long roomInfoId);


}