package com.example.newsfeed.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {

    private String title;

    private String content;

    private String username;

    private Integer like_cnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updatedAt;
}
