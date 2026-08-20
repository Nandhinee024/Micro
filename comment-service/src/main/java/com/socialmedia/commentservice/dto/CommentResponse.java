package com.socialmedia.commentservice.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String userAvatarUrl;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponse() {}

    public CommentResponse(Long id, Long postId, Long userId, String username, String userAvatarUrl, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.userAvatarUrl = userAvatarUrl;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponseBuilder builder() {
        return new CommentResponseBuilder();
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

    public static class CommentResponseBuilder {
        private Long id;
        private Long postId;
        private Long userId;
        private String username;
        private String userAvatarUrl;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CommentResponseBuilder id(Long id) { this.id = id; return this; }
        public CommentResponseBuilder postId(Long postId) { this.postId = postId; return this; }
        public CommentResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public CommentResponseBuilder username(String username) { this.username = username; return this; }
        public CommentResponseBuilder userAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; return this; }
        public CommentResponseBuilder content(String content) { this.content = content; return this; }
        public CommentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CommentResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CommentResponse build() {
            return new CommentResponse(id, postId, userId, username, userAvatarUrl, content, createdAt, updatedAt);
        }
    }
}
