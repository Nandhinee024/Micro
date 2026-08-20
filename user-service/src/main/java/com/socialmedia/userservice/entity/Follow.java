package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows",
    uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @CreationTimestamp
    @Column(name = "followed_at", updatable = false)
    private LocalDateTime followedAt;

    public Follow() {}

    public Follow(Long id, User follower, User following, LocalDateTime followedAt) {
        this.id = id;
        this.follower = follower;
        this.following = following;
        this.followedAt = followedAt;
    }

    public static FollowBuilder builder() {
        return new FollowBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getFollower() { return follower; }
    public void setFollower(User follower) { this.follower = follower; }
    public User getFollowing() { return following; }
    public void setFollowing(User following) { this.following = following; }
    public LocalDateTime getFollowedAt() { return followedAt; }
    public void setFollowedAt(LocalDateTime followedAt) { this.followedAt = followedAt; }

    public static class FollowBuilder {
        private Long id;
        private User follower;
        private User following;
        private LocalDateTime followedAt;

        public FollowBuilder id(Long id) { this.id = id; return this; }
        public FollowBuilder follower(User follower) { this.follower = follower; return this; }
        public FollowBuilder following(User following) { this.following = following; return this; }
        public FollowBuilder followedAt(LocalDateTime followedAt) { this.followedAt = followedAt; return this; }

        public Follow build() {
            return new Follow(id, follower, following, followedAt);
        }
    }
}
