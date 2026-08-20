package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "blocked_users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"}))
public class BlockedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked;

    @CreationTimestamp
    @Column(name = "blocked_at", updatable = false)
    private LocalDateTime blockedAt;

    public BlockedUser() {}

    public BlockedUser(Long id, User blocker, User blocked, LocalDateTime blockedAt) {
        this.id = id;
        this.blocker = blocker;
        this.blocked = blocked;
        this.blockedAt = blockedAt;
    }

    public static BlockedUserBuilder builder() {
        return new BlockedUserBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getBlocker() { return blocker; }
    public void setBlocker(User blocker) { this.blocker = blocker; }
    public User getBlocked() { return blocked; }
    public void setBlocked(User blocked) { this.blocked = blocked; }
    public LocalDateTime getBlockedAt() { return blockedAt; }
    public void setBlockedAt(LocalDateTime blockedAt) { this.blockedAt = blockedAt; }

    public static class BlockedUserBuilder {
        private Long id;
        private User blocker;
        private User blocked;
        private LocalDateTime blockedAt;

        public BlockedUserBuilder id(Long id) { this.id = id; return this; }
        public BlockedUserBuilder blocker(User blocker) { this.blocker = blocker; return this; }
        public BlockedUserBuilder blocked(User blocked) { this.blocked = blocked; return this; }
        public BlockedUserBuilder blockedAt(LocalDateTime blockedAt) { this.blockedAt = blockedAt; return this; }

        public BlockedUser build() {
            return new BlockedUser(id, blocker, blocked, blockedAt);
        }
    }
}
