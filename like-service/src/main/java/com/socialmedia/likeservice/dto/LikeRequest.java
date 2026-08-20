package com.socialmedia.likeservice.dto;

import jakarta.validation.constraints.NotNull;

public class LikeRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username;
    private Long postAuthorId;

    public LikeRequest() {}

    public LikeRequest(Long postId, Long userId, String username, Long postAuthorId) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.postAuthorId = postAuthorId;
    }

    public static LikeRequestBuilder builder() {
        return new LikeRequestBuilder();
    }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getPostAuthorId() { return postAuthorId; }
    public void setPostAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; }

    public static class LikeRequestBuilder {
        private Long postId;
        private Long userId;
        private String username;
        private Long postAuthorId;

        public LikeRequestBuilder postId(Long postId) { this.postId = postId; return this; }
        public LikeRequestBuilder userId(Long userId) { this.userId = userId; return this; }
        public LikeRequestBuilder username(String username) { this.username = username; return this; }
        public LikeRequestBuilder postAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; return this; }

        public LikeRequest build() {
            return new LikeRequest(postId, userId, username, postAuthorId);
        }
    }
}
