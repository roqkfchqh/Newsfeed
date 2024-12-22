package com.example.newsfeed.service.post;

public interface CudStrategy<S, Q> {
    S create(Q q, Long userId);
    S update(Q q, Long entityId, Long userId);
    void delete(Long entityId);
}
