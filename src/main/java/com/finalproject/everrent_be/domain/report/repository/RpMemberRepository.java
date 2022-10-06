package com.finalproject.everrent_be.domain.report.repository;

import com.finalproject.everrent_be.domain.report.model.RpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RpMemberRepository extends JpaRepository<RpUser,Long> {

}
