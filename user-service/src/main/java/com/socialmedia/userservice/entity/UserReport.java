package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser;

    @Column(nullable = false, length = 100)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UserReport() {}

    public UserReport(Long id, User reporter, User reportedUser, String reason, String description, ReportStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.reporter = reporter;
        this.reportedUser = reportedUser;
        this.reason = reason;
        this.description = description;
        this.status = status != null ? status : ReportStatus.PENDING;
        this.createdAt = createdAt;
    }

    public static UserReportBuilder builder() {
        return new UserReportBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }
    public User getReportedUser() { return reportedUser; }
    public void setReportedUser(User reportedUser) { this.reportedUser = reportedUser; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class UserReportBuilder {
        private Long id;
        private User reporter;
        private User reportedUser;
        private String reason;
        private String description;
        private ReportStatus status = ReportStatus.PENDING;
        private LocalDateTime createdAt;

        public UserReportBuilder id(Long id) { this.id = id; return this; }
        public UserReportBuilder reporter(User reporter) { this.reporter = reporter; return this; }
        public UserReportBuilder reportedUser(User reportedUser) { this.reportedUser = reportedUser; return this; }
        public UserReportBuilder reason(String reason) { this.reason = reason; return this; }
        public UserReportBuilder description(String description) { this.description = description; return this; }
        public UserReportBuilder status(ReportStatus status) { this.status = status; return this; }
        public UserReportBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserReport build() {
            return new UserReport(id, reporter, reportedUser, reason, description, status, createdAt);
        }
    }

    public enum ReportStatus {
        PENDING, REVIEWED, DISMISSED, ACTION_TAKEN
    }
}
