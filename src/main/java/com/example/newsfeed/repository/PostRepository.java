package com.example.newsfeed.repository;

import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.example.newsfeed.dto.post.PostResponseDto(p.title, p.content, u.name, p.like_cnt, p.createdAt, p.updatedAt) " +
            "FROM Post p JOIN p.user u WHERE p.id = :postId")
    PostResponseDto findPostWithUsernameById(@Param("postId") Long postId);
}
