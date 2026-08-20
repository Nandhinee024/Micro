package com.socialmedia.likeservice.dto;

public class LikeStatusResponse {
    private Long postId;
    private boolean isLiked;
    private Long likeCount;

    public LikeStatusResponse() {}

    public LikeStatusResponse(Long postId, boolean isLiked, Long likeCount) {
        this.postId = postId;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }

    public static LikeStatusResponseBuilder builder() {
        return new LikeStatusResponseBuilder();
    }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public boolean isLiked() { return isLiked; }
    public void setLiked(boolean liked) { isLiked = liked; }
    public Long getLikeCount() { return likeCount; }
    public void setLikeCount(Long likeCount) { this.likeCount = likeCount; }

    public static class LikeStatusResponseBuilder {
        private Long postId;
        private boolean isLiked;
        private Long likeCount;

        public LikeStatusResponseBuilder postId(Long postId) { this.postId = postId; return this; }
        public LikeStatusResponseBuilder liked(boolean liked) { this.isLiked = liked; return this; }
        public LikeStatusResponseBuilder likeCount(Long likeCount) { this.likeCount = likeCount; return this; }

        public LikeStatusResponse build() {
            return new LikeStatusResponse(postId, isLiked, likeCount);
        }
    }
}
