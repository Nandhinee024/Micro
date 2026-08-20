package com.socialmedia.likeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeStatusResponse {
    private Long postId;
    private Long likeCount;
    private boolean liked;
}
