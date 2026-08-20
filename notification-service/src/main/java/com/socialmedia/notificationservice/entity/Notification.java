package com.socialmedia.notificationservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_username")
    private String senderUsername;

    @Column(name = "sender_avatar_url")
    private String senderAvatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "is_read")
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    public Notification() {}

    public Notification(Long id, Long recipientId, Long senderId, String senderUsername, String senderAvatarUrl, NotificationType type, String message, Long referenceId, String referenceType, boolean isRead, LocalDateTime createdAt, LocalDateTime readAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderAvatarUrl = senderAvatarUrl;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.readAt = readAt;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
    public String getSenderAvatarUrl() { return senderAvatarUrl; }
    public void setSenderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; }
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public static class NotificationBuilder {
        private Long id;
        private Long recipientId;
        private Long senderId;
        private String senderUsername;
        private String senderAvatarUrl;
        private NotificationType type;
        private String message;
        private Long referenceId;
        private String referenceType;
        private boolean isRead = false;
        private LocalDateTime createdAt;
        private LocalDateTime readAt;

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder recipientId(Long recipientId) { this.recipientId = recipientId; return this; }
        public NotificationBuilder senderId(Long senderId) { this.senderId = senderId; return this; }
        public NotificationBuilder senderUsername(String senderUsername) { this.senderUsername = senderUsername; return this; }
        public NotificationBuilder senderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; return this; }
        public NotificationBuilder type(NotificationType type) { this.type = type; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public NotificationBuilder referenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public NotificationBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public NotificationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public NotificationBuilder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }

        public Notification build() {
            return new Notification(id, recipientId, senderId, senderUsername, senderAvatarUrl, type, message, referenceId, referenceType, isRead, createdAt, readAt);
        }
    }

    public enum NotificationType {
        LIKE, COMMENT, FOLLOW, SHARE, SYSTEM
    }
}
