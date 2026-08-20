package com.socialmedia.userservice.repository;

import com.socialmedia.userservice.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    List<UserReport> findByReportedUserId(Long reportedUserId);
    List<UserReport> findByStatus(UserReport.ReportStatus status);
    boolean existsByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);
}
