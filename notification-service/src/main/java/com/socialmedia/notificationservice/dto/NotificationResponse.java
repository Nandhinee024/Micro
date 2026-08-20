package com.socialmedia.notificationservice.dto;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private Long recipientId;
    private Long senderId;
    private String senderUsername;
    private String senderAvatarUrl;
    private String type;
    private String message;
    private Long referenceId;
    private String referenceType;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    public NotificationResponse() {}

    public NotificationResponse(Long id, Long recipientId, Long senderId, String senderUsername, String senderAvatarUrl, String type, String message, Long referenceId, String referenceType, boolean isRead, LocalDateTime createdAt, LocalDateTime readAt) {
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

    public static NotificationResponseBuilder builder() {
        return new NotificationResponseBuilder();
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
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
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

    public static class NotificationResponseBuilder {
        private Long id;
        private Long recipientId;
        private Long senderId;
        private String senderUsername;
        private String senderAvatarUrl;
        private String type;
        private String message;
        private Long referenceId;
        private String referenceType;
        private boolean isRead;
        private LocalDateTime createdAt;
        private LocalDateTime readAt;

        public NotificationResponseBuilder id(Long id) { this.id = id; return this; }
        public NotificationResponseBuilder recipientId(Long recipientId) { this.recipientId = recipientId; return this; }
        public NotificationResponseBuilder senderId(Long senderId) { this.senderId = senderId; return this; }
        public NotificationResponseBuilder senderUsername(String senderUsername) { this.senderUsername = senderUsername; return this; }
        public NotificationResponseBuilder senderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; return this; }
        public NotificationResponseBuilder type(String type) { this.type = type; return this; }
        public NotificationResponseBuilder message(String message) { this.message = message; return this; }
        public NotificationResponseBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public NotificationResponseBuilder referenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public NotificationResponseBuilder isRead(boolean isRead) { this.isRead = isRead; return this; }
        public NotificationResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public NotificationResponseBuilder readAt(LocalDateTime readAt) { this.readAt = readAt; return this; }

        public NotificationResponse build() {
            return new NotificationResponse(id, recipientId, senderId, senderUsername, senderAvatarUrl, type, message, referenceId, referenceType, isRead, createdAt, readAt);
        }
    }
}
