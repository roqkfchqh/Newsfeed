package com.example.newsfeed.dto.user;

import com.example.newsfeed.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateUserResponseDto {

    private final Long id;
    private final String userName;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CreateUserResponseDto of(User user) {
        return CreateUserResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
