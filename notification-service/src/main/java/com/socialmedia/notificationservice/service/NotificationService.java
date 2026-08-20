package com.socialmedia.notificationservice.service;

import com.socialmedia.notificationservice.dto.CreateNotificationRequest;
import com.socialmedia.notificationservice.dto.NotificationResponse;
import com.socialmedia.notificationservice.entity.Notification;
import com.socialmedia.notificationservice.exception.ResourceNotFoundException;
import com.socialmedia.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
            .recipientId(request.getRecipientId())
            .senderId(request.getSenderId())
            .senderUsername(request.getSenderUsername() != null ? request.getSenderUsername() : "User " + request.getSenderId())
            .senderAvatarUrl(request.getSenderAvatarUrl() != null ? request.getSenderAvatarUrl() : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
            .type(request.getType())
            .message(request.getMessage())
            .referenceId(request.getReferenceId())
            .referenceType(request.getReferenceType())
            .isRead(false)
            .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Created notification for recipient: {}, type: {}", saved.getRecipientId(), saved.getType());
        return mapToResponse(saved);
    }

    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByRecipientIdAndIsRead(userId, false);
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        Notification updated = notificationRepository.save(notification);
        return mapToResponse(updated);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByRecipientIdAndIsReadOrderByCreatedAtDesc(userId, false);
        LocalDateTime now = LocalDateTime.now();
        unread.forEach(n -> {
            n.setRead(true);
            n.setReadAt(now);
        });
        notificationRepository.saveAll(unread);
        log.info("Marked all notifications as read for user: {}", userId);
    }

    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        notificationRepository.delete(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
            .id(notification.getId())
            .recipientId(notification.getRecipientId())
            .senderId(notification.getSenderId())
            .senderUsername(notification.getSenderUsername())
            .senderAvatarUrl(notification.getSenderAvatarUrl())
            .type(notification.getType().name())
            .message(notification.getMessage())
            .referenceId(notification.getReferenceId())
            .referenceType(notification.getReferenceType())
            .isRead(notification.isRead())
            .createdAt(notification.getCreatedAt())
            .readAt(notification.getReadAt())
            .build();
    }
}
