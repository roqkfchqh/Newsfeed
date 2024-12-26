package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.BaseResponseDto;

public class BaseResponseMapper {

    public static <T> BaseResponseDto<T> map(T data) {
        return BaseResponseDto.<T>builder()
                .data(data)
                .build();
    }
}

