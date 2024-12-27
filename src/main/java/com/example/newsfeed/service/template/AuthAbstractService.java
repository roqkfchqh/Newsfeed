package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.model.User;

public abstract class AuthAbstractService {

    // Create a new user (signup)
    public final SignupUserResponseDto signup(SignupUserRequestDto signupUserRequestDto) {
        validateExistUserEmail(signupUserRequestDto.getEmail());
        return executeSignup(signupUserRequestDto);
    }

    // Login a user
    public final Long login(LoginUserRequestDto loginUserRequestDto) {
        User user = getUserByEmail(loginUserRequestDto.getEmail());
        validateUserPassword(loginUserRequestDto.getPassword(), user.getPassword());
        return executeLogin(user.getId());
    }

    // getter
    protected abstract User getUserByEmail(String email);

    // validator
    protected abstract void validateExistUserEmail(String email);

    protected abstract void validateUserPassword(String currentPassword, String hashedPassword);

    // business logic
    protected abstract SignupUserResponseDto executeSignup(SignupUserRequestDto signupUserRequestDto);

    protected abstract Long executeLogin(Long userId);
}
