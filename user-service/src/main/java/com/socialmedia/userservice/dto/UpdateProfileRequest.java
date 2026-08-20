package com.socialmedia.userservice.dto;

import java.time.LocalDate;
import java.util.Set;

public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
    private String website;
    private String location;
    private String profilePictureUrl;
    private String coverPhotoUrl;
    private LocalDate dateOfBirth;
    private Set<String> interests;

    public UpdateProfileRequest() {}

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getCoverPhotoUrl() { return coverPhotoUrl; }
    public void setCoverPhotoUrl(String coverPhotoUrl) { this.coverPhotoUrl = coverPhotoUrl; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public Set<String> getInterests() { return interests; }
    public void setInterests(Set<String> interests) { this.interests = interests; }
}
