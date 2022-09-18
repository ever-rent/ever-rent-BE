package com.finalproject.everrent_be.repository;



import com.finalproject.everrent_be.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Member findMemberByEmail(String memberEmail);
    boolean existsByMemberName(String memberName);
    boolean existsByEmail(String email);
}