package com.socialmedia.likeservice.dto;

import java.time.LocalDateTime;

public class LikeResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;

    public LikeResponse() {}

    public LikeResponse(Long id, Long postId, Long userId, String username, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public static LikeResponseBuilder builder() {
        return new LikeResponseBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class LikeResponseBuilder {
        private Long id;
        private Long postId;
        private Long userId;
        private String username;
        private LocalDateTime createdAt;

        public LikeResponseBuilder id(Long id) { this.id = id; return this; }
        public LikeResponseBuilder postId(Long postId) { this.postId = postId; return this; }
        public LikeResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public LikeResponseBuilder username(String username) { this.username = username; return this; }
        public LikeResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public LikeResponse build() {
            return new LikeResponse(id, postId, userId, username, createdAt);
        }
    }
}
