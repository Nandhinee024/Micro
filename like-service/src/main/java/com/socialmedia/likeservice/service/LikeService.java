package com.socialmedia.likeservice.service;

import com.socialmedia.likeservice.dto.LikeRequest;
import com.socialmedia.likeservice.dto.LikeResponse;
import com.socialmedia.likeservice.dto.LikeStatusResponse;
import com.socialmedia.likeservice.entity.Like;
import com.socialmedia.likeservice.exception.DuplicateResourceException;
import com.socialmedia.likeservice.exception.ResourceNotFoundException;
import com.socialmedia.likeservice.feign.NotificationClient;
import com.socialmedia.likeservice.feign.PostClient;
import com.socialmedia.likeservice.repository.LikeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private static final Logger log = LoggerFactory.getLogger(LikeService.class);

    private final LikeRepository likeRepository;
    private final PostClient postClient;
    private final NotificationClient notificationClient;

    public LikeService(LikeRepository likeRepository, PostClient postClient, NotificationClient notificationClient) {
        this.likeRepository = likeRepository;
        this.postClient = postClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public LikeResponse likePost(LikeRequest request) {
        if (likeRepository.existsByPostIdAndUserId(request.getPostId(), request.getUserId())) {
            throw new DuplicateResourceException("User already liked this post");
        }

        Like like = Like.builder()
            .postId(request.getPostId())
            .userId(request.getUserId())
            .username(request.getUsername() != null ? request.getUsername() : "User " + request.getUserId())
            .build();

        Like saved = likeRepository.save(like);
        log.info("User {} liked post {}", request.getUserId(), request.getPostId());

        // Notify post-service to increment count
        try {
            postClient.incrementLike(request.getPostId());
        } catch (Exception e) {
            log.warn("Failed to notify post-service: {}", e.getMessage());
        }

        // Notify notification-service if author is known and not the liker
        if (request.getPostAuthorId() != null && !request.getPostAuthorId().equals(request.getUserId())) {
            try {
                notificationClient.sendNotification(
                    request.getPostAuthorId(),
                    request.getUserId(),
                    like.getUsername(),
                    "LIKE",
                    like.getUsername() + " liked your post",
                    request.getPostId()
                );
            } catch (Exception e) {
                log.warn("Failed to notify notification-service: {}", e.getMessage());
            }
        }

        return mapToResponse(saved);
    }

    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByPostIdAndUserId(postId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Like not found for post " + postId + " and user " + userId));

        likeRepository.delete(like);
        log.info("User {} unliked post {}", userId, postId);

        // Notify post-service to decrement count
        try {
            postClient.decrementLike(postId);
        } catch (Exception e) {
            log.warn("Failed to notify post-service: {}", e.getMessage());
        }
    }

    public LikeStatusResponse getLikeStatus(Long postId, Long userId) {
        long count = likeRepository.countByPostId(postId);
        boolean liked = userId != null && likeRepository.existsByPostIdAndUserId(postId, userId);
        return LikeStatusResponse.builder()
            .postId(postId)
            .likeCount(count)
            .liked(liked)
            .build();
    }

    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public List<LikeResponse> getLikesByPostId(Long postId) {
        return likeRepository.findByPostIdOrderByCreatedAtDesc(postId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private LikeResponse mapToResponse(Like like) {
        return LikeResponse.builder()
            .id(like.getId())
            .postId(like.getPostId())
            .userId(like.getUserId())
            .username(like.getUsername())
            .createdAt(like.getCreatedAt())
            .build();
    }
}
