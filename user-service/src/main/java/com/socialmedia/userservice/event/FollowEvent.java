package com.socialmedia.userservice.event;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FollowEvent implements Serializable {
    private Long recipientId;
    private Long followerId;
    private String followerUsername;
    private LocalDateTime timestamp;

    public FollowEvent() {}

    public FollowEvent(Long recipientId, Long followerId, String followerUsername, LocalDateTime timestamp) {
        this.recipientId = recipientId;
        this.followerId = followerId;
        this.followerUsername = followerUsername;
        this.timestamp = timestamp;
    }

    public static FollowEventBuilder builder() {
        return new FollowEventBuilder();
    }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }
    public Long getFollowerId() { return followerId; }
    public void setFollowerId(Long followerId) { this.followerId = followerId; }
    public String getFollowerUsername() { return followerUsername; }
    public void setFollowerUsername(String followerUsername) { this.followerUsername = followerUsername; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class FollowEventBuilder {
        private Long recipientId;
        private Long followerId;
        private String followerUsername;
        private LocalDateTime timestamp;

        public FollowEventBuilder recipientId(Long recipientId) { this.recipientId = recipientId; return this; }
        public FollowEventBuilder followerId(Long followerId) { this.followerId = followerId; return this; }
        public FollowEventBuilder followerUsername(String followerUsername) { this.followerUsername = followerUsername; return this; }
        public FollowEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public FollowEvent build() {
            return new FollowEvent(recipientId, followerId, followerUsername, timestamp);
        }
    }
}
