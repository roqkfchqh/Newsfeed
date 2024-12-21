package com.example.newsfeed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BaseResponseDto<T> {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final T data;
}
