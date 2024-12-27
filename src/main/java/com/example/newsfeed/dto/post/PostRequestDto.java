package com.example.newsfeed.dto.post;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @Size(min = 4, max = 20, message = "제목은 4-20자 사이로 작성해주세요.")
    private String title;

    @Size(min = 10, max = 200, message = "내용은 10-200자 사이로 작성해주세요.")
    private String content;
}
