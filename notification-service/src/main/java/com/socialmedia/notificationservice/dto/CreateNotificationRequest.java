package com.socialmedia.notificationservice.dto;

import com.socialmedia.notificationservice.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
