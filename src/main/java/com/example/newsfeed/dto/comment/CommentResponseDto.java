package com.example.newsfeed.dto.comment;

import com.example.newsfeed.model.Comment;
import lombok.*;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final Long postId;
    private final Long userId;
    private final String username;
    private final String content;
    private final Integer likeCount;

    @Builder
    public CommentResponseDto(Long id, Long postId, Long userId, String username, String content, Integer likeCount) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.likeCount = likeCount;
    }

}
