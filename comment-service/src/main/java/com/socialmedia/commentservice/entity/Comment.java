package com.socialmedia.commentservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "user_avatar_url")
    private String userAvatarUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment() {}

    public Comment(Long id, Long postId, Long userId, String username, String userAvatarUrl, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.userAvatarUrl = userAvatarUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentBuilder builder() {
        return new CommentBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserAvatarUrl() { return userAvatarUrl; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class CommentBuilder {
        private Long id;
        private Long postId;
        private Long userId;
        private String username;
        private String userAvatarUrl;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CommentBuilder id(Long id) { this.id = id; return this; }
        public CommentBuilder postId(Long postId) { this.postId = postId; return this; }
        public CommentBuilder userId(Long userId) { this.userId = userId; return this; }
        public CommentBuilder username(String username) { this.username = username; return this; }
        public CommentBuilder userAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; return this; }
        public CommentBuilder content(String content) { this.content = content; return this; }
        public CommentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CommentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Comment build() {
            return new Comment(id, postId, userId, username, userAvatarUrl, content, createdAt, updatedAt);
        }
    }
}
