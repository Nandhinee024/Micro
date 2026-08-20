package com.socialmedia.userservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "is_verified")
    private boolean isVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PrivacySetting privacySetting;

    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();

    public User() {}

    public User(Long id, String username, String email, String password, String firstName, String lastName, String phoneNumber, UserRole role, AccountStatus status, boolean isVerified, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLoginAt, UserProfile profile, PrivacySetting privacySetting, Set<String> interests) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role != null ? role : UserRole.USER;
        this.status = status != null ? status : AccountStatus.ACTIVE;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.profile = profile;
        this.privacySetting = privacySetting;
        this.interests = interests != null ? interests : new HashSet<>();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public UserProfile getProfile() { return profile; }
    public void setProfile(UserProfile profile) { this.profile = profile; }
    public PrivacySetting getPrivacySetting() { return privacySetting; }
    public void setPrivacySetting(PrivacySetting privacySetting) { this.privacySetting = privacySetting; }
    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private UserRole role = UserRole.USER;
        private AccountStatus status = AccountStatus.ACTIVE;
        private boolean isVerified = false;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime lastLoginAt;
        private UserProfile profile;
        private PrivacySetting privacySetting;
        private Set<String> interests = new HashSet<>();

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder username(String username) { this.username = username; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserBuilder role(UserRole role) { this.role = role; return this; }
        public UserBuilder status(AccountStatus status) { this.status = status; return this; }
        public UserBuilder isVerified(boolean isVerified) { this.isVerified = isVerified; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public UserBuilder lastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }
        public UserBuilder profile(UserProfile profile) { this.profile = profile; return this; }
        public UserBuilder privacySetting(PrivacySetting privacySetting) { this.privacySetting = privacySetting; return this; }
        public UserBuilder interests(Set<String> interests) { this.interests = interests; return this; }

        public User build() {
            return new User(id, username, email, password, firstName, lastName, phoneNumber, role, status, isVerified, createdAt, updatedAt, lastLoginAt, profile, privacySetting, interests);
        }
    }

    public enum UserRole {
        USER, ADMIN, MODERATOR, CONTENT_CREATOR
    }

    public enum AccountStatus {
        ACTIVE, SUSPENDED, BANNED, DEACTIVATED
    }
}
