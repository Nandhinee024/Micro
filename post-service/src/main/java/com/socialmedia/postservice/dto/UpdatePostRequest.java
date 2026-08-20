package com.socialmedia.postservice.dto;

import com.socialmedia.postservice.entity.Post;
import jakarta.validation.constraints.Size;

public class UpdatePostRequest {

    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    private String content;

    private String mediaUrl;
    private Post.PostVisibility visibility;

    public UpdatePostRequest() {}

    public UpdatePostRequest(String content, String mediaUrl, Post.PostVisibility visibility) {
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.visibility = visibility;
    }

    public static UpdatePostRequestBuilder builder() {
        return new UpdatePostRequestBuilder();
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }
    public Post.PostVisibility getVisibility() { return visibility; }
    public void setVisibility(Post.PostVisibility visibility) { this.visibility = visibility; }

    public static class UpdatePostRequestBuilder {
        private String content;
        private String mediaUrl;
        private Post.PostVisibility visibility;

        public UpdatePostRequestBuilder content(String content) { this.content = content; return this; }
        public UpdatePostRequestBuilder mediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; return this; }
        public UpdatePostRequestBuilder visibility(Post.PostVisibility visibility) { this.visibility = visibility; return this; }

        public UpdatePostRequest build() {
            return new UpdatePostRequest(content, mediaUrl, visibility);
        }
    }
}
