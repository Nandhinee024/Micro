package com.socialmedia.postservice.repository;

import com.socialmedia.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatusOrderByCreatedAtDesc(Post.PostStatus status);
    List<Post> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Post.PostStatus status);
    List<Post> findByOrderByCreatedAtDesc();
    long countByUserId(Long userId);
}
