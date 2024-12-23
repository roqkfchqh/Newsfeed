package com.example.newsfeed.dto.user;

import com.example.newsfeed.model.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UpdateUserNameResponseDto {

    private final String updatedName;
    private final LocalDateTime updatedAt;

    public static UpdateUserNameResponseDto of(User user) {
        return UpdateUserNameResponseDto.builder()
                .updatedName(user.getName())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
