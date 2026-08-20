package com.socialmedia.postservice.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserClientFallback implements UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public Map<String, Object> getUserById(Long id) {
        log.warn("User service unavailable. Returning empty user for id: {}", id);
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("id", id);
        fallback.put("username", "unknown");
        return fallback;
    }
}
