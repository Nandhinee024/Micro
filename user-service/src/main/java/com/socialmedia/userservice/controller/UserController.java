package com.socialmedia.userservice.controller;

import com.socialmedia.userservice.dto.UpdateProfileRequest;
import com.socialmedia.userservice.dto.UserResponse;
import com.socialmedia.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponse> updateProfile(
        @PathVariable Long id,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateProfile(id, request));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<Map<String, String>> followUser(
        @PathVariable Long id,
        @RequestParam(required = false) Long followerId,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long currentUserId = followerId != null ? followerId : headerUserId;
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "followerId is required"));
        }
        userService.followUser(currentUserId, id);
        return ResponseEntity.ok(Map.of("message", "User followed successfully"));
    }

    @DeleteMapping("/{id}/follow")
    public ResponseEntity<Map<String, String>> unfollowUser(
        @PathVariable Long id,
        @RequestParam(required = false) Long followerId,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        Long currentUserId = followerId != null ? followerId : headerUserId;
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "followerId is required"));
        }
        userService.unfollowUser(currentUserId, id);
        return ResponseEntity.ok(Map.of("message", "User unfollowed successfully"));
    }

    @GetMapping("/{id}/is-following")
    public ResponseEntity<Map<String, Boolean>> isFollowing(
        @PathVariable Long id,
        @RequestParam Long followerId
    ) {
        boolean following = userService.isFollowing(followerId, id);
        return ResponseEntity.ok(Map.of("isFollowing", following));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFollowers(id));
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFollowing(id));
    }
}
