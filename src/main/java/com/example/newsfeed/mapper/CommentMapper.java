package com.example.newsfeed.mapper;

import com.example.newsfeed.model.Comment;
import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.model.CommentLike;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;

public class CommentMapper {

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .content(comment.getContent())
                .likeCount(comment.getLike_cnt())
                .build();
    }

    public static Comment toEntity(User user, Post post, String content){
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .likeCnt(0)
                .build();
    }

    public static CommentLike toEntity(User user, Comment comment){
        return CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();
    }
}