package com.example.newsfeed.service.validate_template;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;

public abstract class AuthAbstractService {

    // service logic
    /**
     * 유저 생성
     */
    public final SignupUserResponseDto signup(SignupUserRequestDto signupUserRequestDto) {
        validateExistUserEmail(signupUserRequestDto.getEmail());
        return executeSignup(signupUserRequestDto);
    }

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
    protected abstract void validateExistUserEmail(String email);
    protected abstract void validateUserPassword(Long userId, String currentPassword);

    // business logic
    protected abstract SignupUserResponseDto executeSignup(SignupUserRequestDto signupUserRequestDto);
    protected abstract Long executeLogin(Long userId);
}
