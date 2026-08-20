package com.socialmedia.userservice.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationClientFallback implements NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClientFallback.class);

    @Override
    public void sendFollowNotification(Long recipientId, Long followerId, String followerUsername) {
        log.warn("Notification service is unavailable. Follow notification not sent for recipient: {}", recipientId);
    }
}
