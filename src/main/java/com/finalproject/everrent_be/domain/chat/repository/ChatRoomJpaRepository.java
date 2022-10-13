package com.finalproject.everrent_be.domain.chat.repository;

import com.finalproject.everrent_be.domain.chat.model.ChatRoom;
import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByRoomId(String roomId);
    void deleteByRoomId(String roomId);
    Optional<ChatRoom> findByMemberAndProduct(Member member, Product product);

    List<ChatRoom> findAllByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);
}