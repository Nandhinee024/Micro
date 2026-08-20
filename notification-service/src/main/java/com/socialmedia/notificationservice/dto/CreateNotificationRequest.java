package com.socialmedia.notificationservice.dto;

import com.socialmedia.notificationservice.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNotificationRequest {

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    private Long senderId;
    private String senderUsername;
    private String senderAvatarUrl;

    @NotNull(message = "Notification type is required")
    private Notification.NotificationType type;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    private Long referenceId;
    private String referenceType;

    public CreateNotificationRequest() {}

    public CreateNotificationRequest(Long recipientId, Long senderId, String senderUsername, String senderAvatarUrl, Notification.NotificationType type, String message, Long referenceId, String referenceType) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.senderAvatarUrl = senderAvatarUrl;
        this.type = type;
        this.message = message;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
    }

    public static CreateNotificationRequestBuilder builder() {
        return new CreateNotificationRequestBuilder();
    }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getSenderUsername() { return senderUsername; }
    public void setSenderUsername(String senderUsername) { this.senderUsername = senderUsername; }
    public String getSenderAvatarUrl() { return senderAvatarUrl; }
    public void setSenderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; }
    public Notification.NotificationType getType() { return type; }
    public void setType(Notification.NotificationType type) { this.type = type; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public static class CreateNotificationRequestBuilder {
        private Long recipientId;
        private Long senderId;
        private String senderUsername;
        private String senderAvatarUrl;
        private Notification.NotificationType type;
        private String message;
        private Long referenceId;
        private String referenceType;

        public CreateNotificationRequestBuilder recipientId(Long recipientId) { this.recipientId = recipientId; return this; }
        public CreateNotificationRequestBuilder senderId(Long senderId) { this.senderId = senderId; return this; }
        public CreateNotificationRequestBuilder senderUsername(String senderUsername) { this.senderUsername = senderUsername; return this; }
        public CreateNotificationRequestBuilder senderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; return this; }
        public CreateNotificationRequestBuilder type(Notification.NotificationType type) { this.type = type; return this; }
        public CreateNotificationRequestBuilder message(String message) { this.message = message; return this; }
        public CreateNotificationRequestBuilder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public CreateNotificationRequestBuilder referenceType(String referenceType) { this.referenceType = referenceType; return this; }

        public CreateNotificationRequest build() {
            return new CreateNotificationRequest(recipientId, senderId, senderUsername, senderAvatarUrl, type, message, referenceId, referenceType);
        }
    }
}
