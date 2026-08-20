package com.socialmedia.commentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username;
    private String userAvatarUrl;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String content;

    private Long postAuthorId;

    public CreateCommentRequest() {}

    public CreateCommentRequest(Long postId, Long userId, String username, String userAvatarUrl, String content, Long postAuthorId) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.userAvatarUrl = userAvatarUrl;
        this.content = content;
        this.postAuthorId = postAuthorId;
    }

    public static CreateCommentRequestBuilder builder() {
        return new CreateCommentRequestBuilder();
    }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserAvatarUrl() { return userAvatarUrl; }
    public void setUserAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getPostAuthorId() { return postAuthorId; }
    public void setPostAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; }

    public static class CreateCommentRequestBuilder {
        private Long postId;
        private Long userId;
        private String username;
        private String userAvatarUrl;
        private String content;
        private Long postAuthorId;

        public CreateCommentRequestBuilder postId(Long postId) { this.postId = postId; return this; }
        public CreateCommentRequestBuilder userId(Long userId) { this.userId = userId; return this; }
        public CreateCommentRequestBuilder username(String username) { this.username = username; return this; }
        public CreateCommentRequestBuilder userAvatarUrl(String userAvatarUrl) { this.userAvatarUrl = userAvatarUrl; return this; }
        public CreateCommentRequestBuilder content(String content) { this.content = content; return this; }
        public CreateCommentRequestBuilder postAuthorId(Long postAuthorId) { this.postAuthorId = postAuthorId; return this; }

        public CreateCommentRequest build() {
            return new CreateCommentRequest(postId, userId, username, userAvatarUrl, content, postAuthorId);
        }
    }
}
