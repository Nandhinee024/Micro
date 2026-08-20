package com.socialmedia.likeservice.repository;

import com.socialmedia.likeservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    void deleteByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    List<Like> findByPostIdOrderByCreatedAtDesc(Long postId);
    List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);
}
