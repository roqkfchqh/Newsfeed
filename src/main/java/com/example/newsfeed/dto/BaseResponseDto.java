package com.example.newsfeed.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BaseResponseDto<T> {
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final T data;
}
