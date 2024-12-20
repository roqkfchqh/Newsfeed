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

    public static PostResponseDto toCreate(String title, String content, String username, LocalDateTime createdAt) {
        return PostResponseDto.builder()
                .title(title)
                .content(content)
                .username(username)
                .createdAt(createdAt)
                .build();
    }

    public static PostResponseDto toUpdate(String title, String content, String username, LocalDateTime updatedAt){
        return PostResponseDto.builder()
                .title(title)
                .content(content)
                .username(username)
                .updatedAt(updatedAt)
                .build();
    }
}
