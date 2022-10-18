package com.finalproject.everrent_be.domain.chat.repository;

import com.finalproject.everrent_be.domain.chat.model.InvitedMembers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitedMembersRepository extends JpaRepository<InvitedMembers, Long> {
    void deleteByMemberIdAndRoomId(Long memberId, String roomId);
    boolean existsByMemberIdAndRoomId(Long memberId, String roomId);
    Optional<InvitedMembers> findByMemberIdAndRoomId(Long memberId, String roomId);
    List<InvitedMembers> findAllByMemberId(Long memberId);
    List<InvitedMembers> findAllByRoomId(String roomId);
    List<InvitedMembers> findAllByMemberIdAndReadCheck(Long memberId, Boolean readCheck);
    List<InvitedMembers> findByRoomId(String roomId);
    void deleteAllByMemberId(Long memberId);
}
