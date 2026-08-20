package com.socialmedia.notificationservice.kafka;

import com.socialmedia.notificationservice.dto.CreateNotificationRequest;
import com.socialmedia.notificationservice.entity.Notification;
import com.socialmedia.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);

    private final NotificationService notificationService;

    public NotificationKafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-follow-topic", groupId = "notification-group")
    public void consumeFollowEvent(Map<String, Object> event) {
        try {
            log.info("Received follow event from Kafka: {}", event);
            Long recipientId = Long.valueOf(event.get("recipientId").toString());
            Long followerId = Long.valueOf(event.get("followerId").toString());
            String followerUsername = (String) event.get("followerUsername");

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
        } catch (Exception e) {
            log.warn("Error processing follow event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "post-like-topic", groupId = "notification-group")
    public void consumeLikeEvent(Map<String, Object> event) {
        try {
            log.info("Received like event from Kafka: {}", event);
            String action = (String) event.get("action");
            if ("LIKE".equalsIgnoreCase(action) && event.get("postAuthorId") != null) {
                Long postAuthorId = Long.valueOf(event.get("postAuthorId").toString());
                Long userId = Long.valueOf(event.get("userId").toString());
                String username = (String) event.get("username");
                Long postId = Long.valueOf(event.get("postId").toString());

                if (!postAuthorId.equals(userId)) {
                    CreateNotificationRequest request = CreateNotificationRequest.builder()
                        .recipientId(postAuthorId)
                        .senderId(userId)
                        .senderUsername(username)
                        .type(Notification.NotificationType.LIKE)
                        .message(username + " liked your post")
                        .referenceId(postId)
                        .referenceType("POST")
                        .build();
                    notificationService.createNotification(request);
                }
            }
        } catch (Exception e) {
            log.warn("Error processing like event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "post-comment-topic", groupId = "notification-group")
    public void consumeCommentEvent(Map<String, Object> event) {
        try {
            log.info("Received comment event from Kafka: {}", event);
            String action = (String) event.get("action");
            if ("CREATED".equalsIgnoreCase(action) && event.get("postAuthorId") != null) {
                Long postAuthorId = Long.valueOf(event.get("postAuthorId").toString());
                Long userId = Long.valueOf(event.get("userId").toString());
                String username = (String) event.get("username");
                String content = (String) event.get("content");
                Long postId = Long.valueOf(event.get("postId").toString());

                if (!postAuthorId.equals(userId)) {
                    String snippet = content != null && content.length() > 30 ? content.substring(0, 30) + "..." : content;
                    CreateNotificationRequest request = CreateNotificationRequest.builder()
                        .recipientId(postAuthorId)
                        .senderId(userId)
                        .senderUsername(username)
                        .type(Notification.NotificationType.COMMENT)
                        .message(username + " commented: " + snippet)
                        .referenceId(postId)
                        .referenceType("POST")
                        .build();
                    notificationService.createNotification(request);
                }
            }
        } catch (Exception e) {
            log.warn("Error processing comment event: {}", e.getMessage());
        }
    }
}
