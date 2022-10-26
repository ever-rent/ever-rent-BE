package com.finalproject.everrent_be.domain.report.repository;

import com.finalproject.everrent_be.domain.report.model.RpPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RpPostRepository extends JpaRepository<RpPost, Long> {

}
