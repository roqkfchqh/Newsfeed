package com.example.newsfeed.repository;

import com.example.newsfeed.dto.post.ReadPageResponseDto;
import com.example.newsfeed.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new com.example.newsfeed.dto.post.ReadPageResponseDto(c.id, c.content, c.user.name, c.like_cnt, c.createdAt, c.updatedAt) " +
            "FROM Comment c " +
            "WHERE c.post.id = :postId")
    Page<ReadPageResponseDto> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);
}
