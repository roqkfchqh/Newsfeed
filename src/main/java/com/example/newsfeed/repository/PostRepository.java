package com.example.newsfeed.repository;

import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //hot-data (like_cnt >= 30)
    @Query("SELECT new com.example.newsfeed.dto.post.PostResponseDto( " +
            "CASE WHEN p.like_cnt >= 30 THEN CONCAT('🔥 ', p.title) ELSE p.title END, " +
            "p.content, u.name, p.like_cnt, p.createdAt, p.updatedAt) " +
            "FROM Post p " +
            "JOIN p.user u " +
            "WHERE u.id IN (SELECT f.followee.id FROM Friend f WHERE f.follower.id = :userId AND f.follow = true)")
    List<PostResponseDto> findPostsByFriends(@Param("userId") Long userId, Sort sort);
}
