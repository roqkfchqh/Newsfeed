package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.model.Comment;
import com.example.newsfeed.model.CommentLike;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;

public class CommentMapper {

    // Comment Entity to Comment Response Dto
    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .likeCount(comment.getLike_cnt())
                .build();
    }

    //  Comment with User, Post Entity to Comment Entity
    public static Comment toEntity(User user, Post post, String content) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .likeCnt(0)
                .build();
    }


    // Comment Entity with User Entity to CommentLike Entity
    public static CommentLike toEntity(User user, Comment comment) {
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
    }
}