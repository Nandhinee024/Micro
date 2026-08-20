package com.socialmedia.commentservice.service;

import com.socialmedia.commentservice.dto.CommentResponse;
import com.socialmedia.commentservice.dto.CreateCommentRequest;
import com.socialmedia.commentservice.entity.Comment;
import com.socialmedia.commentservice.exception.ResourceNotFoundException;
import com.socialmedia.commentservice.feign.NotificationClient;
import com.socialmedia.commentservice.feign.PostClient;
import com.socialmedia.commentservice.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostClient postClient;
    private final NotificationClient notificationClient;

    public CommentService(CommentRepository commentRepository, PostClient postClient, NotificationClient notificationClient) {
        this.commentRepository = commentRepository;
        this.postClient = postClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public CommentResponse addComment(CreateCommentRequest request) {
        Comment comment = Comment.builder()
            .postId(request.getPostId())
            .userId(request.getUserId())
            .username(request.getUsername() != null ? request.getUsername() : "User " + request.getUserId())
            .userAvatarUrl(request.getUserAvatarUrl() != null ? request.getUserAvatarUrl() : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
            .content(request.getContent().trim())
            .build();

        Comment saved = commentRepository.save(comment);
        log.info("User {} commented on post {}", request.getUserId(), request.getPostId());

        // Notify post-service to increment comment count
        try {
            postClient.incrementComment(request.getPostId());
        } catch (Exception e) {
            log.warn("Failed to notify post-service: {}", e.getMessage());
        }

        // Notify post author if known and not the commenter
        if (request.getPostAuthorId() != null && !request.getPostAuthorId().equals(request.getUserId())) {
            try {
                notificationClient.sendNotification(
                    request.getPostAuthorId(),
                    request.getUserId(),
                    comment.getUsername(),
                    "COMMENT",
                    comment.getUsername() + " commented: " + (comment.getContent().length() > 30 ? comment.getContent().substring(0, 30) + "..." : comment.getContent()),
                    request.getPostId()
                );
            } catch (Exception e) {
                log.warn("Failed to notify notification-service: {}", e.getMessage());
            }
        }

        return mapToResponse(saved);
    }

    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public CommentResponse getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        return mapToResponse(comment);
    }

    public long getCommentCount(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        Long postId = comment.getPostId();
        commentRepository.delete(comment);
        log.info("Deleted comment with id: {}", id);

        // Notify post-service to decrement comment count
        try {
            postClient.decrementComment(postId);
        } catch (Exception e) {
            log.warn("Failed to notify post-service: {}", e.getMessage());
        }
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .postId(comment.getPostId())
            .userId(comment.getUserId())
            .username(comment.getUsername())
            .userAvatarUrl(comment.getUserAvatarUrl())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }
}
