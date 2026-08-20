package com.socialmedia.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String bio;
    private String website;
    private String location;
    private LocalDate dateOfBirth;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private Set<String> interests;
}
