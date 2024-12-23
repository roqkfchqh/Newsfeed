package com.example.newsfeed.service;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;

public abstract class AuthAbstractService {

    /**
     * 로그인
     */
    public final Long login(LoginUserRequestDto loginUserRequestDto) {
        return executeLogin(loginUserRequestDto);
    }

    // validator
    protected abstract void validateUserEmail(String email);
    protected abstract void validateUserPassword(Long userId, String currentPassword);

    // business logic
    protected abstract Long executeLogin(LoginUserRequestDto loginUserRequestDto);
    protected abstract CreateUserResponseDto executeSignup(CreateUserRequestDto createUserRequestDto);
}
