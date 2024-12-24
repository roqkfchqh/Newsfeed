package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.model.User;

public class AuthMapper {

    // Entity to Dto
    public static SignupUserResponseDto toDto(User user) {
        return SignupUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // DTO to Entity
    public static User toEntity(SignupUserRequestDto signupUserRequestDto, String hashedPassword) {
        return User.builder()
                .name(signupUserRequestDto.getName())
                .email(signupUserRequestDto.getEmail())
                .password(hashedPassword)
                .build();
    }
}
