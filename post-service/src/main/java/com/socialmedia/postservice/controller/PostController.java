package com.socialmedia.postservice.controller;

import com.socialmedia.postservice.dto.CreatePostRequest;
import com.socialmedia.postservice.dto.PostResponse;
import com.socialmedia.postservice.dto.UpdatePostRequest;
import com.socialmedia.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
        @Valid @RequestBody CreatePostRequest request,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        if (request.getUserId() == null && headerUserId != null) {
            request.setUserId(headerUserId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(request));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
        @PathVariable Long id,
        @Valid @RequestBody UpdatePostRequest request
    ) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
    }

    @PutMapping("/{id}/increment-like")
    public ResponseEntity<Void> incrementLike(@PathVariable Long id) {
        postService.incrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/decrement-like")
    public ResponseEntity<Void> decrementLike(@PathVariable Long id) {
        postService.decrementLikeCount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/increment-comment")
    public ResponseEntity<Void> incrementComment(@PathVariable Long id) {
        postService.incrementCommentCount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/decrement-comment")
    public ResponseEntity<Void> decrementComment(@PathVariable Long id) {
        postService.decrementCommentCount(id);
        return ResponseEntity.ok().build();
    }
}
