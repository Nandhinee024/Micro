package com.socialmedia.likeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username")
    private String username;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Like() {}

    public Like(Long id, Long postId, Long userId, String username, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
    }

    public static LikeBuilder builder() {
        return new LikeBuilder();
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

    public static class LikeBuilder {
        private Long id;
        private Long postId;
        private Long userId;
        private String username;
        private LocalDateTime createdAt;

        public LikeBuilder id(Long id) { this.id = id; return this; }
        public LikeBuilder postId(Long postId) { this.postId = postId; return this; }
        public LikeBuilder userId(Long userId) { this.userId = userId; return this; }
        public LikeBuilder username(String username) { this.username = username; return this; }
        public LikeBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Like build() {
            return new Like(id, postId, userId, username, createdAt);
        }
    }
}
