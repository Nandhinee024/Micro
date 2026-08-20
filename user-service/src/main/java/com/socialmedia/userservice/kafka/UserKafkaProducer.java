package com.socialmedia.userservice.kafka;

import com.socialmedia.userservice.event.FollowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(UserKafkaProducer.class);
    public static final String FOLLOW_TOPIC = "user-follow-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFollowEvent(FollowEvent event) {
        try {
            log.info("Publishing FollowEvent to Kafka topic '{}': recipient={}, follower={}",
                FOLLOW_TOPIC, event.getRecipientId(), event.getFollowerUsername());
            kafkaTemplate.send(FOLLOW_TOPIC, event.getRecipientId().toString(), event);
        } catch (Exception e) {
            log.warn("Could not publish follow event to Kafka: {}", e.getMessage());
        }
    }
}
