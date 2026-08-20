package com.socialmedia.userservice.service;

import com.socialmedia.userservice.dto.UpdateProfileRequest;
import com.socialmedia.userservice.dto.UserResponse;
import com.socialmedia.userservice.entity.*;
import com.socialmedia.userservice.exception.BadRequestException;
import com.socialmedia.userservice.exception.DuplicateResourceException;
import com.socialmedia.userservice.exception.ResourceNotFoundException;
import com.socialmedia.userservice.feign.NotificationClient;
import com.socialmedia.userservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BlockedUserRepository blockedUserRepository;
    private final FollowRepository followRepository;
    private final UserReportRepository userReportRepository;
    private final NotificationClient notificationClient;

    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, BlockedUserRepository blockedUserRepository, FollowRepository followRepository, UserReportRepository userReportRepository, NotificationClient notificationClient) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.blockedUserRepository = blockedUserRepository;
        this.followRepository = followRepository;
        this.userReportRepository = userReportRepository;
        this.notificationClient = notificationClient;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::mapToUserResponse)
            .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return mapToUserResponse(user);
    }

    public List<UserResponse> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.searchByUsernameOrName(query.trim())
            .stream()
            .map(this::mapToUserResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getInterests() != null) user.setInterests(request.getInterests());

        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = UserProfile.builder().user(user).build();
        }
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getWebsite() != null) profile.setWebsite(request.getWebsite());
        if (request.getLocation() != null) profile.setLocation(request.getLocation());
        if (request.getDateOfBirth() != null) profile.setDateOfBirth(request.getDateOfBirth());
        if (request.getProfilePictureUrl() != null) profile.setProfilePictureUrl(request.getProfilePictureUrl());
        if (request.getCoverPhotoUrl() != null) profile.setCoverPhotoUrl(request.getCoverPhotoUrl());
        user.setProfile(profile);

        return mapToUserResponse(userRepository.save(user));
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new BadRequestException("You cannot follow yourself");
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new DuplicateResourceException("Already following this user");
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new ResourceNotFoundException("Follower not found with id: " + followerId));
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new ResourceNotFoundException("User to follow not found with id: " + followingId));

        Follow follow = Follow.builder().follower(follower).following(following).build();
        followRepository.save(follow);

        if (following.getProfile() == null) {
            following.setProfile(UserProfile.builder().user(following).build());
        }
        if (follower.getProfile() == null) {
            follower.setProfile(UserProfile.builder().user(follower).build());
        }
        following.getProfile().setFollowerCount(following.getProfile().getFollowerCount() + 1);
        follower.getProfile().setFollowingCount(follower.getProfile().getFollowingCount() + 1);
        userRepository.save(following);
        userRepository.save(follower);

        try {
            notificationClient.sendFollowNotification(followingId, followerId, follower.getUsername());
        } catch (Exception e) {
            log.warn("Notification service unavailable: {}", e.getMessage());
        }
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        if (!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new BadRequestException("Not following this user");
        }
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);

        User follower = userRepository.findById(followerId).orElse(null);
        User following = userRepository.findById(followingId).orElse(null);
        if (following != null && following.getProfile() != null) {
            following.getProfile().setFollowerCount(Math.max(0, following.getProfile().getFollowerCount() - 1));
            userRepository.save(following);
        }
        if (follower != null && follower.getProfile() != null) {
            follower.getProfile().setFollowingCount(Math.max(0, follower.getProfile().getFollowingCount() - 1));
            userRepository.save(follower);
        }
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    public List<UserResponse> getFollowers(Long userId) {
        return followRepository.findByFollowingId(userId).stream()
            .map(f -> mapToUserResponse(f.getFollower()))
            .collect(Collectors.toList());
    }

    public List<UserResponse> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId).stream()
            .map(f -> mapToUserResponse(f.getFollowing()))
            .collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        UserProfile profile = user.getProfile();
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole() != null ? user.getRole().name() : "USER")
            .status(user.getStatus() != null ? user.getStatus().name() : "ACTIVE")
            .isVerified(user.isVerified())
            .interests(user.getInterests())
            .profilePictureUrl(profile != null ? profile.getProfilePictureUrl() : null)
            .bio(profile != null ? profile.getBio() : null)
            .followerCount(profile != null ? profile.getFollowerCount() : 0L)
            .followingCount(profile != null ? profile.getFollowingCount() : 0L)
            .postCount(profile != null ? profile.getPostCount() : 0L)
            .createdAt(user.getCreatedAt())
            .build();
    }
}
