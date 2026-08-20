package com.socialmedia.commentservice.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentEvent implements Serializable {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private Long postAuthorId;
    private String action; // "CREATED" or "DELETED"
    private LocalDateTime timestamp;

    public CommentEvent() {}

    public CommentEvent(Long commentId, Long postId, Long userId, String username, String content, Long postAuthorId, String action, LocalDateTime timestamp) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.postAuthorId = postAuthorId;
        this.action = action;
        this.timestamp = timestamp;
    }

    public static CommentEventBuilder builder() {
        return new CommentEventBuilder();
    }

    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getPostAuthorId() { return postAuthorId; }
    public void setPostAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class CommentEventBuilder {
        private Long commentId;
        private Long postId;
        private Long userId;
        private String username;
        private String content;
        private Long postAuthorId;
        private String action;
        private LocalDateTime timestamp;

        public CommentEventBuilder commentId(Long commentId) { this.commentId = commentId; return this; }
        public CommentEventBuilder postId(Long postId) { this.postId = postId; return this; }
        public CommentEventBuilder userId(Long userId) { this.userId = userId; return this; }
        public CommentEventBuilder username(String username) { this.username = username; return this; }
        public CommentEventBuilder content(String content) { this.content = content; return this; }
        public CommentEventBuilder postAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; return this; }
        public CommentEventBuilder action(String action) { this.action = action; return this; }
        public CommentEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public CommentEvent build() {
            return new CommentEvent(commentId, postId, userId, username, content, postAuthorId, action, timestamp);
        }
    }
}
