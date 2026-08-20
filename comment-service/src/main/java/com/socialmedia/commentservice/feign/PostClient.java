package com.socialmedia.commentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "post-service")
public interface PostClient {

    @PutMapping("/api/posts/{id}/increment-comment")
    void incrementComment(@PathVariable("id") Long id);

    @PutMapping("/api/posts/{id}/decrement-comment")
    void decrementComment(@PathVariable("id") Long id);
}
