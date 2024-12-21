package com.example.newsfeed.dto.comment;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {
    private Long postId;
    private String contents;
}
