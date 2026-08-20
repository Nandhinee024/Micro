package com.socialmedia.postservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "author_username")
    private String authorUsername;

    @Column(name = "author_avatar_url")
    private String authorAvatarUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "media_url", length = 1000)
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostVisibility visibility = PostVisibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.ACTIVE;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "comment_count")
    private Long commentCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Post() {}

    public Post(Long id, Long userId, String authorUsername, String authorAvatarUrl, String content, String mediaUrl, PostVisibility visibility, PostStatus status, Long likeCount, Long commentCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.authorUsername = authorUsername;
        this.authorAvatarUrl = authorAvatarUrl;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.visibility = visibility != null ? visibility : PostVisibility.PUBLIC;
        this.status = status != null ? status : PostStatus.ACTIVE;
        this.likeCount = likeCount != null ? likeCount : 0L;
        this.commentCount = commentCount != null ? commentCount : 0L;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostBuilder builder() {
        return new PostBuilder();
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
    public PostVisibility getVisibility() { return visibility; }
    public void setVisibility(PostVisibility visibility) { this.visibility = visibility; }
    public PostStatus getStatus() { return status; }
    public void setStatus(PostStatus status) { this.status = status; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }
    public Long getCommentCount() { return commentCount; }
    public void setCommentCount(Long commentCount) { this.commentCount = commentCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class PostBuilder {
        private Long id;
        private Long userId;
        private String authorUsername;
        private String authorAvatarUrl;
        private String content;
        private String mediaUrl;
        private PostVisibility visibility = PostVisibility.PUBLIC;
        private PostStatus status = PostStatus.ACTIVE;
        private Long likeCount = 0L;
        private Long commentCount = 0L;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PostBuilder id(Long id) { this.id = id; return this; }
        public PostBuilder userId(Long userId) { this.userId = userId; return this; }
        public PostBuilder authorUsername(String authorUsername) { this.authorUsername = authorUsername; return this; }
        public PostBuilder authorAvatarUrl(String authorAvatarUrl) { this.authorAvatarUrl = authorAvatarUrl; return this; }
        public PostBuilder content(String content) { this.content = content; return this; }
        public PostBuilder mediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; return this; }
        public PostBuilder visibility(PostVisibility visibility) { this.visibility = visibility; return this; }
        public PostBuilder status(PostStatus status) { this.status = status; return this; }
        public PostBuilder likeCount(Long likeCount) { this.likeCount = likeCount; return this; }
        public PostBuilder commentCount(Long commentCount) { this.commentCount = commentCount; return this; }
        public PostBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PostBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Post build() {
            return new Post(id, userId, authorUsername, authorAvatarUrl, content, mediaUrl, visibility, status, likeCount, commentCount, createdAt, updatedAt);
        }
    }

    public enum PostVisibility {
        PUBLIC, FRIENDS, PRIVATE
    }

    public enum PostStatus {
        ACTIVE, DELETED
    }
}
