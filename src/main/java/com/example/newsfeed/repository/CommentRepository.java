package com.example.newsfeed.repository;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT new com.example.newsfeed.dto.comment.CommentResponseDto(c.id, c.post.id, c.user.id, c.user.name, c.content, c.like_cnt) " +
            "FROM Comment c " +
            "JOIN c.user u " +
            "WHERE c.post.id = :postId AND u.deletedAt IS NULL")
    Page<CommentResponseDto> findCommentsByPostId(@Param("postId") Long postId, Pageable pageable);
}
