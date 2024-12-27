package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;

import java.util.List;

public class PostMapper {

    // Post Entity to Post Response Dto
    public static PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getName())
                .like_cnt(post.getLike_cnt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // Post Entity with Comment List to Post Response Dto
    public static PostResponseDto toDto(Post post, List<CommentResponseDto> comment) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getName())
                .like_cnt(post.getLike_cnt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .comments(comment)
                .build();
    }

    // Post with User Entity to Post Entity
    public static Post toEntity(String title, String content, User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
