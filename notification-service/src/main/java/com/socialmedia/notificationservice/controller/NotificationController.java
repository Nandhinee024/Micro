package com.socialmedia.notificationservice.controller;

import com.socialmedia.notificationservice.dto.CreateNotificationRequest;
import com.socialmedia.notificationservice.dto.NotificationResponse;
import com.socialmedia.notificationservice.entity.Notification;
import com.socialmedia.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
        @Valid @RequestBody CreateNotificationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(
        @RequestParam Long recipientId,
        @RequestParam Long senderId,
        @RequestParam String senderUsername,
        @RequestParam String type,
        @RequestParam String message,
        @RequestParam(required = false) Long referenceId
    ) {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
            .recipientId(recipientId)
            .senderId(senderId)
            .senderUsername(senderUsername)
            .type(Notification.NotificationType.valueOf(type.toUpperCase()))
            .message(message)
            .referenceId(referenceId)
            .referenceType("POST")
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @PostMapping("/follow")
    public ResponseEntity<Void> sendFollowNotification(
        @RequestParam Long recipientId,
        @RequestParam Long followerId,
        @RequestParam String followerUsername
    ) {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
            .recipientId(recipientId)
            .senderId(followerId)
            .senderUsername(followerUsername)
            .type(Notification.NotificationType.FOLLOW)
            .message(followerUsername + " started following you")
            .referenceId(followerId)
            .referenceType("USER")
            .build();
        notificationService.createNotification(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
        @RequestParam(required = false) Long userId,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long currentUserId = userId != null ? userId : headerUserId;
        if (currentUserId == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(currentUserId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(Map.of("unreadCount", notificationService.getUnreadCount(userId)));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(Map.of("message", "Notification deleted successfully"));
    }
}
