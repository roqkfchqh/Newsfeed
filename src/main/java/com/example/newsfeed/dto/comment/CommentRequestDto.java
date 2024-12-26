package com.example.newsfeed.dto.comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {
    private Long postId;
    private String content;
}
