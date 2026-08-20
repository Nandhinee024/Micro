package com.socialmedia.postservice.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public Map<String, Object> getUserById(Long id) {
        log.warn("User service unavailable. Returning empty user for id: {}", id);
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("id", id);
        fallback.put("username", "unknown");
        return fallback;
    }
}
