package com.example.newsfeed.dto.post;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {

    @Size()
    private String title;

    @Size()
    private String content;
}
