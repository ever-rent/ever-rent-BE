package com.finalproject.everrent_be.domain.chat.repository;

import com.finalproject.everrent_be.domain.chat.model.RoomDetail;
import com.finalproject.everrent_be.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomDetailRepository extends JpaRepository<RoomDetail, Long> {

    List<RoomDetail> findAllByMemberOrderByModifiedAtDesc(Member member);
    Optional<RoomDetail> findByRoomInfo_IdAndMember_IdAndProduct_Id(Long infoId, Long memberId, Long productId);
    Optional<RoomDetail> findByMember_IdAndProduct_Id(Long memberId, Long itemId);
    Optional<RoomDetail> findByRoomInfo_IdAndMember_Id(Long infoId, Long memberId);
}