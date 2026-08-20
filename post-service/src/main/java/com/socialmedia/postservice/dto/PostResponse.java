package com.socialmedia.postservice.dto;

import java.time.LocalDateTime;

public class PostResponse {
    private Long id;
    private Long userId;
    private String authorUsername;
    private String authorAvatarUrl;
    private String content;
    private String mediaUrl;
    private String visibility;
    private String status;
    private Long likeCount;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse() {}

    public PostResponse(Long id, Long userId, String authorUsername, String authorAvatarUrl, String content, String mediaUrl, String visibility, String status, Long likeCount, Long commentCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.authorUsername = authorUsername;
        this.authorAvatarUrl = authorAvatarUrl;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.visibility = visibility;
        this.status = status;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostResponseBuilder builder() {
        return new PostResponseBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
    public String getAuthorAvatarUrl() { return authorAvatarUrl; }
    public void setAuthorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getCommentCount() { return commentCount; }
    public void setCommentCount(Long commentCount) { this.commentCount = commentCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class PostResponseBuilder {
        private Long id;
        private Long userId;
        private String authorUsername;
        private String authorAvatarUrl;
        private String content;
        private String mediaUrl;
        private String visibility;
        private String status;
        private Long likeCount;
        private Long commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PostResponseBuilder id(Long id) { this.id = id; return this; }
        public PostResponseBuilder userId(Long userId) { this.userId = userId; return this; }
        public PostResponseBuilder authorUsername(String authorUsername) { this.authorUsername = authorUsername; return this; }
        public PostResponseBuilder authorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; return this; }
        public PostResponseBuilder content(String content) { this.content = content; return this; }
        public PostResponseBuilder mediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; return this; }
        public PostResponseBuilder visibility(String visibility) { this.visibility = visibility; return this; }
        public PostResponseBuilder status(String status) { this.status = status; return this; }
        public PostResponseBuilder likeCount(Long likeCount) { this.likeCount = likeCount; return this; }
        public PostResponseBuilder commentCount(Long commentCount) { this.commentCount = commentCount; return this; }
        public PostResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PostResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public PostResponse build() {
            return new PostResponse(id, userId, authorUsername, authorAvatarUrl, content, mediaUrl, visibility, status, likeCount, commentCount, createdAt, updatedAt);
        }
    }
}
