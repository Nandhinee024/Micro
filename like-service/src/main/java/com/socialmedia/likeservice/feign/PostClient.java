package com.socialmedia.likeservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "post-service")
public interface PostClient {

    @PutMapping("/api/posts/{id}/increment-like")
    void incrementLike(@PathVariable("id") Long id);

    @PutMapping("/api/posts/{id}/decrement-like")
    void decrementLike(@PathVariable("id") Long id);
}
