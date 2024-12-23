package com.example.newsfeed.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginUserRequestDto {
    private final String email;
    private final String password;
}
