package com.socialmedia.postservice.kafka;

import com.socialmedia.postservice.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PostKafkaConsumer.class);

    private final PostService postService;

    public PostKafkaConsumer(PostService postService) {
        this.postService = postService;
    }

    @KafkaListener(topics = "post-like-topic", groupId = "post-service-group")
    public void consumeLikeEvent(Map<String, Object> event) {
        try {
            log.info("Post Service received LikeEvent from Kafka: {}", event);
            Long postId = Long.valueOf(event.get("postId").toString());
            String action = (String) event.get("action");

            if ("LIKE".equalsIgnoreCase(action)) {
                postService.incrementLikeCount(postId);
            } else if ("UNLIKE".equalsIgnoreCase(action)) {
                postService.decrementLikeCount(postId);
            }
        } catch (Exception e) {
            log.warn("Error processing like count in post-service: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "post-comment-topic", groupId = "post-service-group")
    public void consumeCommentEvent(Map<String, Object> event) {
        try {
            log.info("Post Service received CommentEvent from Kafka: {}", event);
            Long postId = Long.valueOf(event.get("postId").toString());
            String action = (String) event.get("action");

            if ("CREATED".equalsIgnoreCase(action)) {
                postService.incrementCommentCount(postId);
            } else if ("DELETED".equalsIgnoreCase(action)) {
                postService.decrementCommentCount(postId);
            }
        } catch (Exception e) {
            log.warn("Error processing comment count in post-service: {}", e.getMessage());
        }
    }
}
