package com.socialmedia.postservice.service;

import com.socialmedia.postservice.dto.CreatePostRequest;
import com.socialmedia.postservice.dto.PostResponse;
import com.socialmedia.postservice.dto.UpdatePostRequest;
import com.socialmedia.postservice.entity.Post;
import com.socialmedia.postservice.exception.ResourceNotFoundException;
import com.socialmedia.postservice.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Post post = Post.builder()
            .userId(request.getUserId())
            .authorUsername(request.getAuthorUsername() != null ? request.getAuthorUsername() : "User " + request.getUserId())
            .authorAvatarUrl(request.getAuthorAvatarUrl() != null ? request.getAuthorAvatarUrl() : "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150")
            .content(request.getContent())
            .mediaUrl(request.getMediaUrl())
            .visibility(request.getVisibility() != null ? request.getVisibility() : Post.PostVisibility.PUBLIC)
            .status(Post.PostStatus.ACTIVE)
            .likeCount(0L)
            .commentCount(0L)
            .build();

        Post saved = postRepository.save(post);
        log.info("Created new post with ID: {} for user: {}", saved.getId(), saved.getUserId());
        return mapToResponse(saved);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findByStatusOrderByCreatedAtDesc(Post.PostStatus.ACTIVE)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
            .filter(p -> p.getStatus() == Post.PostStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return mapToResponse(post);
    }

    public List<PostResponse> getPostsByUserId(Long userId) {
        return postRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, Post.PostStatus.ACTIVE)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse updatePost(Long id, UpdatePostRequest request) {
        Post post = postRepository.findById(id)
            .filter(p -> p.getStatus() == Post.PostStatus.ACTIVE)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            post.setContent(request.getContent().trim());
        }
        if (request.getMediaUrl() != null) {
            post.setMediaUrl(request.getMediaUrl());
        }
        if (request.getVisibility() != null) {
            post.setVisibility(request.getVisibility());
        }

        Post updated = postRepository.save(post);
        return mapToResponse(updated);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setStatus(Post.PostStatus.DELETED);
        postRepository.save(post);
        log.info("Marked post with ID: {} as deleted", id);
    }

    @Transactional
    public void incrementLikeCount(Long id) {
        postRepository.findById(id).ifPresent(p -> {
            p.setLikeCount(p.getLikeCount() + 1);
            postRepository.save(p);
        });
    }

    @Transactional
    public void decrementLikeCount(Long id) {
        postRepository.findById(id).ifPresent(p -> {
            p.setLikeCount(Math.max(0, p.getLikeCount() - 1));
            postRepository.save(p);
        });
    }

    @Transactional
    public void incrementCommentCount(Long id) {
        postRepository.findById(id).ifPresent(p -> {
            p.setCommentCount(p.getCommentCount() + 1);
            postRepository.save(p);
        });
    }

    @Transactional
    public void decrementCommentCount(Long id) {
        postRepository.findById(id).ifPresent(p -> {
            p.setCommentCount(Math.max(0, p.getCommentCount() - 1));
            postRepository.save(p);
        });
    }

    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
            .id(post.getId())
            .userId(post.getUserId())
            .authorUsername(post.getAuthorUsername())
            .authorAvatarUrl(post.getAuthorAvatarUrl())
            .content(post.getContent())
            .mediaUrl(post.getMediaUrl())
            .visibility(post.getVisibility().name())
            .status(post.getStatus().name())
            .likeCount(post.getLikeCount())
            .commentCount(post.getCommentCount())
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .build();
    }
}
