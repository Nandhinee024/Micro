package com.socialmedia.likeservice.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class LikeEvent implements Serializable {
    private Long postId;
    private Long userId;
    private String username;
    private Long postAuthorId;
    private String action; // "LIKE" or "UNLIKE"
    private LocalDateTime timestamp;

    public LikeEvent() {}

    public LikeEvent(Long postId, Long userId, String username, Long postAuthorId, String action, LocalDateTime timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.postAuthorId = postAuthorId;
        this.action = action;
        this.timestamp = timestamp;
    }

    public static LikeEventBuilder builder() {
        return new LikeEventBuilder();
    }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getPostAuthorId() { return postAuthorId; }
    public void setPostAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class LikeEventBuilder {
        private Long postId;
        private Long userId;
        private String username;
        private Long postAuthorId;
        private String action;
        private LocalDateTime timestamp;

        public LikeEventBuilder postId(Long postId) { this.postId = postId; return this; }
        public LikeEventBuilder userId(Long userId) { this.userId = userId; return this; }
        public LikeEventBuilder username(String username) { this.username = username; return this; }
        public LikeEventBuilder postAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; return this; }
        public LikeEventBuilder action(String action) { this.action = action; return this; }
        public LikeEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public LikeEvent build() {
            return new LikeEvent(postId, userId, username, postAuthorId, action, timestamp);
        }
    }
}
