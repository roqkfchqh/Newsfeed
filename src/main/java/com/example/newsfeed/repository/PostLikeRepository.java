package com.example.newsfeed.repository;

import com.example.newsfeed.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);

    @Query("SELECT pl FROM PostLike pl JOIN pl.user u WHERE pl.post.id = :postId AND u.id = :userId AND u.deletedAt IS NULL")
    Optional<PostLike> findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
