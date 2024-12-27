package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.auth.AuthMessageResponseDto;
import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.mapper.BaseResponseMapper;
import com.example.newsfeed.service.AuthService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Create a new user (Sign up) (POST /auth/signup)
    @PostMapping("/signup")
    public ResponseEntity<BaseResponseDto<SignupUserResponseDto>> signup(@Valid @RequestBody SignupUserRequestDto signupUserRequestDto) {
        SignupUserResponseDto data = authService.signup(signupUserRequestDto);

        // Use BaseResponseMapper to map the response
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Login (POST /auth/login)
    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<AuthMessageResponseDto>> login(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto, HttpServletRequest request) {
        Long userId = authService.login(loginUserRequestDto);
        SessionUserUtils.setId(userId, request);

        AuthMessageResponseDto data = new AuthMessageResponseDto("로그인에 성공하였습니다.");

        // Use BaseResponseMapper to map the response
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Logout (POST /auth/logout)
    @PostMapping("/logout")
    public ResponseEntity<BaseResponseDto<AuthMessageResponseDto>> logout(HttpServletRequest request) {
        SessionUserUtils.invalidate(request);

        AuthMessageResponseDto data = new AuthMessageResponseDto("로그아웃 되었습니다.");

        // Use BaseResponseMapper to map the response
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }
}
