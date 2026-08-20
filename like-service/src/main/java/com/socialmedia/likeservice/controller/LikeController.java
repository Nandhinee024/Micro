package com.socialmedia.likeservice.controller;

import com.socialmedia.likeservice.dto.LikeRequest;
import com.socialmedia.likeservice.dto.LikeResponse;
import com.socialmedia.likeservice.dto.LikeStatusResponse;
import com.socialmedia.likeservice.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<LikeResponse> likePost(
        @Valid @RequestBody LikeRequest request,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        if (request.getUserId() == null && headerUserId != null) {
            request.setUserId(headerUserId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.likePost(request));
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> unlikePost(
        @RequestParam Long postId,
        @RequestParam(required = false) Long userId,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long currentUserId = userId != null ? userId : headerUserId;
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
        }
        likeService.unlikePost(postId, currentUserId);
        return ResponseEntity.ok(Map.of("message", "Post unliked successfully"));
    }

    @GetMapping("/post/{postId}/status")
    public ResponseEntity<LikeStatusResponse> getLikeStatus(
        @PathVariable Long postId,
        @RequestParam(required = false) Long userId,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long currentUserId = userId != null ? userId : headerUserId;
        return ResponseEntity.ok(likeService.getLikeStatus(postId, currentUserId));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long postId) {
        return ResponseEntity.ok(Map.of("likeCount", likeService.getLikeCount(postId)));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeResponse>> getLikesByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikesByPostId(postId));
    }
}
