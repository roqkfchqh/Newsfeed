package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;
import com.example.newsfeed.dto.user.FetchUserResponseDto;
import com.example.newsfeed.dto.user.UpdateUserNameResponseDto;
import com.example.newsfeed.model.User;

public class UserMapper {

    // Entity to DTO
    public static CreateUserResponseDto toCreateUserResponseDto(User user) {
        return CreateUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static UpdateUserNameResponseDto toUpdateUserBaneResponseDto(User user) {
        return UpdateUserNameResponseDto.builder()
                .updatedName(user.getName())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static FetchUserResponseDto toFetchUserResponseDto(User user) {
        return FetchUserResponseDto.builder()
                .userName(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // DTO to Entity
    public static User fromCreateUserRequestDto(CreateUserRequestDto createUserRequestDto, String hashedPassword) {
        return User.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(hashedPassword)
                .build();
    }
}
