package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;

import java.util.List;

public class PostMapper {

    public static PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getName())
                .like_cnt(post.getLike_cnt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static PostResponseDto toDto(Post post, List<CommentResponseDto> comment) {
        return PostResponseDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getName())
                .like_cnt(post.getLike_cnt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(comment)
                .build();
    }

    public static Post toEntity(String title, String content, User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
