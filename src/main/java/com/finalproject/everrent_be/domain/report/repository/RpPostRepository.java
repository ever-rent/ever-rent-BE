package com.finalproject.everrent_be.domain.report.repository;

import com.finalproject.everrent_be.domain.member.model.Member;
import com.finalproject.everrent_be.domain.report.model.RpPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RpPostRepository extends JpaRepository<RpPost, Long> {

    List<RpPost> findAllByWhoisRpId(Long id);
}
