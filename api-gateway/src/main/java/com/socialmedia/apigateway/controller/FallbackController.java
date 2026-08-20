package com.socialmedia.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/user-service")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "User Service is currently starting or unavailable. Please try again."));
    }

    @RequestMapping("/post-service")
    public ResponseEntity<Map<String, String>> postServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Post Service is currently starting or unavailable. Please try again."));
    }

    @RequestMapping("/like-service")
    public ResponseEntity<Map<String, String>> likeServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Like Service is currently starting or unavailable. Please try again."));
    }

    @RequestMapping("/comment-service")
    public ResponseEntity<Map<String, String>> commentServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Comment Service is currently starting or unavailable. Please try again."));
    }

    @RequestMapping("/notification-service")
    public ResponseEntity<Map<String, String>> notificationServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("message", "Notification Service is currently starting or unavailable. Please try again."));
    }
}
