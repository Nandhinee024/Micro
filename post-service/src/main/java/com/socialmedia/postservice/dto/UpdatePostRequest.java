package com.socialmedia.postservice.dto;

import com.socialmedia.postservice.entity.Post;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {

    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    private String content;

    private String mediaUrl;

    private Post.PostVisibility visibility;
}
