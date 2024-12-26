package com.example.newsfeed.dto.comment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {
    //validate 추가
    //comment 수정 시 postId 필요없음
    private Long postId;
    private String content;
}
