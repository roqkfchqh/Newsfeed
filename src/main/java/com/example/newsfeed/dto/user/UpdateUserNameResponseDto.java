package com.example.newsfeed.dto.user;

import com.example.newsfeed.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UpdateUserNameResponseDto {

    private final String updatedName;
    private final LocalDateTime updatedAt;
}
