package com.socialmedia.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service", fallback = NotificationClientFallback.class)
public interface NotificationClient {

    @PostMapping("/api/notifications/follow")
    void sendFollowNotification(
        @RequestParam("recipientId") Long recipientId,
        @RequestParam("followerId") Long followerId,
        @RequestParam("followerUsername") String followerUsername
    );
}
