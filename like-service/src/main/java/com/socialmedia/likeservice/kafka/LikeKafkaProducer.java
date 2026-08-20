package com.socialmedia.likeservice.kafka;

import com.socialmedia.likeservice.event.LikeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(LikeKafkaProducer.class);
    public static final String LIKE_TOPIC = "post-like-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public LikeKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLikeEvent(LikeEvent event) {
        try {
            log.info("Publishing LikeEvent to Kafka topic '{}': post={}, user={}, action={}",
                LIKE_TOPIC, event.getPostId(), event.getUsername(), event.getAction());
            kafkaTemplate.send(LIKE_TOPIC, event.getPostId().toString(), event);
        } catch (Exception e) {
            log.warn("Could not publish like event to Kafka: {}", e.getMessage());
        }
    }
}
