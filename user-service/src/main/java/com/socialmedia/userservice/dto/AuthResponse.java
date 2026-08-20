package com.socialmedia.userservice.dto;

public class AuthResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private String bio;
    private String message;

    public AuthResponse() {}

    public AuthResponse(Long id, String username, String email, String firstName, String lastName, String profilePictureUrl, String bio, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.message = message;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
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
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static class AuthResponseBuilder {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String profilePictureUrl;
        private String bio;
        private String message;

        public AuthResponseBuilder id(Long id) { this.id = id; return this; }
        public AuthResponseBuilder username(String username) { this.username = username; return this; }
        public AuthResponseBuilder email(String email) { this.email = email; return this; }
        public AuthResponseBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public AuthResponseBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public AuthResponseBuilder profilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; return this; }
        public AuthResponseBuilder bio(String bio) { this.bio = bio; return this; }
        public AuthResponseBuilder message(String message) { this.message = message; return this; }

        public AuthResponse build() {
            return new AuthResponse(id, username, email, firstName, lastName, profilePictureUrl, bio, message);
        }
    }
}
