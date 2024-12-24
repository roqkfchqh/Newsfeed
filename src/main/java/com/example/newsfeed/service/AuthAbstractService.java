package com.example.newsfeed.service;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;

public abstract class AuthAbstractService {

    /**
     * 로그인
     */
    public final Long login(LoginUserRequestDto loginUserRequestDto) {
        Long userId = getUserIdByEmail(loginUserRequestDto.getEmail());
        validateUserPassword(userId, loginUserRequestDto.getPassword());
        return executeLogin(userId);
    }

    // getter
    protected abstract Long getUserIdByEmail(String email);

    // validator
    protected abstract void validateUserPassword(Long userId, String currentPassword);

    // business logic
    protected abstract Long executeLogin(Long userId);
    protected abstract CreateUserResponseDto executeSignup(CreateUserRequestDto createUserRequestDto);
}