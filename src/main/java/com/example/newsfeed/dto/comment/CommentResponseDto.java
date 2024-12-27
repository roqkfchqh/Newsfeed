package com.example.newsfeed.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private final Long id;
    private final Long postId;
    private final Long userId;
    private final String username;
    private final String content;
    private final Integer likeCount;
}
