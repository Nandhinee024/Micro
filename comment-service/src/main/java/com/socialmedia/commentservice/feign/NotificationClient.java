package com.socialmedia.commentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service")
public interface NotificationClient {

    @PostMapping("/api/notifications/send")
    void sendNotification(
        @RequestParam("recipientId") Long recipientId,
        @RequestParam("senderId") Long senderId,
        @RequestParam("senderUsername") String senderUsername,
        @RequestParam("type") String type,
        @RequestParam("message") String message,
        @RequestParam("referenceId") Long referenceId
    );
}
