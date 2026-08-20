package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "cover_photo_url")
    private String coverPhotoUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 100)
    private String website;

    @Column(length = 100)
    private String location;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "follower_count")
    private Long followerCount = 0L;

    @Column(name = "following_count")
    private Long followingCount = 0L;

    @Column(name = "post_count")
    private Long postCount = 0L;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UserProfile() {}

    public UserProfile(Long id, User user, String profilePictureUrl, String coverPhotoUrl, String bio, String website, String location, LocalDate dateOfBirth, Long followerCount, Long followingCount, Long postCount, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.profilePictureUrl = profilePictureUrl;
        this.coverPhotoUrl = coverPhotoUrl;
        this.bio = bio;
        this.website = website;
        this.location = location;
        this.dateOfBirth = dateOfBirth;
        this.followerCount = followerCount != null ? followerCount : 0L;
        this.followingCount = followingCount != null ? followingCount : 0L;
        this.postCount = postCount != null ? postCount : 0L;
        this.updatedAt = updatedAt;
    }

    public static UserProfileBuilder builder() {
        return new UserProfileBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getCoverPhotoUrl() { return coverPhotoUrl; }
    public void setCoverPhotoUrl(String coverPhotoUrl) { this.coverPhotoUrl = coverPhotoUrl; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Long getFollowerCount() { return followerCount; }
    public void setFollowerCount(Long followerCount) { this.followerCount = followerCount; }
    public Long getFollowingCount() { return followingCount; }
    public void setFollowingCount(Long followingCount) { this.followingCount = followingCount; }
    public Long getPostCount() { return postCount; }
    public void setPostCount(Long postCount) { this.postCount = postCount; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class UserProfileBuilder {
        private Long id;
        private User user;
        private String profilePictureUrl;
        private String coverPhotoUrl;
        private String bio;
        private String website;
        private String location;
        private LocalDate dateOfBirth;
        private Long followerCount = 0L;
        private Long followingCount = 0L;
        private Long postCount = 0L;
        private LocalDateTime updatedAt;

        public UserProfileBuilder id(Long id) { this.id = id; return this; }
        public UserProfileBuilder user(User user) { this.user = user; return this; }
        public UserProfileBuilder profilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; return this; }
        public UserProfileBuilder coverPhotoUrl(String coverPhotoUrl) { this.coverPhotoUrl = coverPhotoUrl; return this; }
        public UserProfileBuilder bio(String bio) { this.bio = bio; return this; }
        public UserProfileBuilder website(String website) { this.website = website; return this; }
        public UserProfileBuilder location(String location) { this.location = location; return this; }
        public UserProfileBuilder dateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; return this; }
        public UserProfileBuilder followerCount(Long followerCount) { this.followerCount = followerCount; return this; }
        public UserProfileBuilder followingCount(Long followingCount) { this.followingCount = followingCount; return this; }
        public UserProfileBuilder postCount(Long postCount) { this.postCount = postCount; return this; }
        public UserProfileBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public UserProfile build() {
            return new UserProfile(id, user, profilePictureUrl, coverPhotoUrl, bio, website, location, dateOfBirth, followerCount, followingCount, postCount, updatedAt);
        }
    }
}
