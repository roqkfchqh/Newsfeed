package com.example.newsfeed.repository;

import com.example.newsfeed.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p " +
            "FROM Post p " +
            "JOIN p.user u " +
            "JOIN Friend f ON f.followee.id = u.id " +
            "WHERE f.follower.id = :userId AND f.follow = true AND u.deletedAt IS NULL")
    List<Post> findPostsByFriends(@Param("userId") Long userId, Sort sort);

    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.id = :postId AND p.user.deletedAt IS NULL")
    Optional<Post> findPostWithUser(@Param("postId") Long postId);
}
