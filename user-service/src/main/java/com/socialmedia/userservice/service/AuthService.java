package com.socialmedia.userservice.service;

import com.socialmedia.userservice.dto.AuthResponse;
import com.socialmedia.userservice.dto.LoginRequest;
import com.socialmedia.userservice.dto.RegisterRequest;
import com.socialmedia.userservice.entity.PrivacySetting;
import com.socialmedia.userservice.entity.User;
import com.socialmedia.userservice.entity.UserProfile;
import com.socialmedia.userservice.exception.BadRequestException;
import com.socialmedia.userservice.exception.DuplicateResourceException;
import com.socialmedia.userservice.exception.ResourceNotFoundException;
import com.socialmedia.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(request.getPassword())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phoneNumber(request.getPhoneNumber())
            .build();

        UserProfile profile = UserProfile.builder()
            .user(user)
            .bio("Hello, I am using the Social Media Platform!")
            .profilePictureUrl("https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
            .build();
        user.setProfile(profile);

        PrivacySetting privacySetting = PrivacySetting.builder()
            .user(user)
            .build();
        user.setPrivacySetting(privacySetting);

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .profilePictureUrl(profile.getProfilePictureUrl())
            .bio(profile.getBio())
            .message("User registered successfully")
            .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsernameOrEmail())
            .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username or email: " + request.getUsernameOrEmail())));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        UserProfile profile = user.getProfile();

        return AuthResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .profilePictureUrl(profile != null ? profile.getProfilePictureUrl() : null)
            .bio(profile != null ? profile.getBio() : null)
            .message("Login successful")
            .build();
    }
}
