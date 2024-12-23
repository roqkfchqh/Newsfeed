package com.example.newsfeed.repository;

import com.example.newsfeed.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    PostLike findByPostIdAndUserId(Long postId, Long userId);
}
