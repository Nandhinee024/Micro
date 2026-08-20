package com.socialmedia.userservice.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationClientFallback implements NotificationClient {

    @Override
    public void sendFollowNotification(Long recipientId, Long followerId, String followerUsername) {
        log.warn("Notification service is unavailable. Follow notification not sent for recipient: {}", recipientId);
    }
}
