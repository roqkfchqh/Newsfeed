package com.example.newsfeed.service;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;

public interface AuthService {

    Long login(LoginUserRequestDto loginUserRequestDto);
}
