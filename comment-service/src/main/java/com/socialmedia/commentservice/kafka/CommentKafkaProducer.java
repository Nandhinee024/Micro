package com.socialmedia.commentservice.kafka;

import com.socialmedia.commentservice.event.CommentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CommentKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(CommentKafkaProducer.class);
    public static final String COMMENT_TOPIC = "post-comment-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CommentKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCommentEvent(CommentEvent event) {
        try {
            log.info("Publishing CommentEvent to Kafka topic '{}': post={}, user={}, action={}",
                COMMENT_TOPIC, event.getPostId(), event.getUsername(), event.getAction());
            kafkaTemplate.send(COMMENT_TOPIC, event.getPostId().toString(), event);
        } catch (Exception e) {
            log.warn("Could not publish comment event to Kafka: {}", e.getMessage());
        }
    }
}
