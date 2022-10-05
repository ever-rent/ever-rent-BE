package com.finalproject.everrent_be.domain.report.repository;

import com.finalproject.everrent_be.domain.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
