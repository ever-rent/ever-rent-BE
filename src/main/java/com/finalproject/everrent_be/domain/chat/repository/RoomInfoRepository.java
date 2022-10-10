package com.finalproject.everrent_be.domain.chat.repository;

import com.finalproject.everrent_be.domain.chat.model.RoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomInfoRepository extends JpaRepository<RoomInfo, Long> {
    Optional<RoomInfo> findByMember_IdAndProduct_Id(Long memberId, Long productId);
}