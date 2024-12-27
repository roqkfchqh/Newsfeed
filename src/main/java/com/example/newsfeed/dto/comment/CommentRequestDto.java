package com.example.newsfeed.dto.comment;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {

    private Long postId;

    @Size(min = 10, max = 200, message = "내용은 10-200자 사이로 작성해주세요.")
    private String content;
}
