package com.socialmedia.commentservice.controller;

import com.socialmedia.commentservice.dto.CommentResponse;
import com.socialmedia.commentservice.dto.CreateCommentRequest;
import com.socialmedia.commentservice.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
        @Valid @RequestBody CreateCommentRequest request,
        @RequestHeader(value = "X-User-Id", required = false) Long headerUserId
    ) {
        if (request.getUserId() == null && headerUserId != null) {
            request.setUserId(headerUserId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Long>> getCommentCount(@PathVariable Long postId) {
        return ResponseEntity.ok(Map.of("commentCount", commentService.getCommentCount(postId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(Map.of("message", "Comment deleted successfully"));
    }
}
