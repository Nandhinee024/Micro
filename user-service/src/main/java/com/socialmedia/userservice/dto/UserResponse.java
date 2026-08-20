package com.socialmedia.userservice.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private boolean isVerified;
    private Set<String> interests;
    private String profilePictureUrl;
    private String bio;
    private Long followerCount;
    private Long followingCount;
    private Long postCount;
    private LocalDateTime createdAt;

    public UserResponse() {}

    public UserResponse(Long id, String username, String email, String firstName, String lastName, String role, String status, boolean isVerified, Set<String> interests, String profilePictureUrl, String bio, Long followerCount, Long followingCount, Long postCount, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.status = status;
        this.isVerified = isVerified;
        this.interests = interests;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.postCount = postCount;
        this.createdAt = createdAt;
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public Long getFollowerCount() { return followerCount; }
    public void setFollowerCount(Long followerCount) { this.followerCount = followerCount; }
    public Long getFollowingCount() { return followingCount; }
    public void setFollowingCount(Long followingCount) { this.followingCount = followingCount; }
    public Long getPostCount() { return postCount; }
    public void setPostCount(Long postCount) { this.postCount = postCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class UserResponseBuilder {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String role;
        private String status;
        private boolean isVerified;
        private Set<String> interests;
        private String profilePictureUrl;
        private String bio;
        private Long followerCount;
        private Long followingCount;
        private Long postCount;
        private LocalDateTime createdAt;

        public UserResponseBuilder id(Long id) { this.id = id; return this; }
        public UserResponseBuilder username(String username) { this.username = username; return this; }
        public UserResponseBuilder email(String email) { this.email = email; return this; }
        public UserResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserResponseBuilder role(String role) { this.role = role; return this; }
        public UserResponseBuilder status(String status) { this.status = status; return this; }
        public UserResponseBuilder isVerified(boolean isVerified) { this.isVerified = isVerified; return this; }
        public UserResponseBuilder interests(Set<String> interests) { this.interests = interests; return this; }
        public UserResponseBuilder profilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; return this; }
        public UserResponseBuilder bio(String bio) { this.bio = bio; return this; }
        public UserResponseBuilder followerCount(Long followerCount) { this.followerCount = followerCount; return this; }
        public UserResponseBuilder followingCount(Long followingCount) { this.followingCount = followingCount; return this; }
        public UserResponseBuilder postCount(Long postCount) { this.postCount = postCount; return this; }
        public UserResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public UserResponse build() {
            return new UserResponse(id, username, email, firstName, lastName, role, status, isVerified, interests, profilePictureUrl, bio, followerCount, followingCount, postCount, createdAt);
        }
    }
}
