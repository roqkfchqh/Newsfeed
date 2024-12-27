package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.user.FetchUserResponseDto;
import com.example.newsfeed.dto.user.UpdateUserNameResponseDto;
import com.example.newsfeed.model.User;

public class UserMapper {

    // User Entity to Update Username Response Dto
    public static UpdateUserNameResponseDto toUpdateUserNameResponseDto(User user) {
        return UpdateUserNameResponseDto.builder()
                .updatedName(user.getName())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // User Entity to Fetch User Response Dto
    public static FetchUserResponseDto toFetchUserResponseDto(User user) {
        return FetchUserResponseDto.builder()
                .userName(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
