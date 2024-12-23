package com.example.newsfeed.controller;

import com.example.newsfeed.dto.auth.AuthMessageResponseDto;
import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.service.AuthServiceImpl;
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

    private final AuthServiceImpl authService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<AuthMessageResponseDto> login(
            @Valid @RequestBody LoginUserRequestDto loginUserRequestDto,
            HttpServletRequest request
    ) {
        // 검증을 위한 서비스 호출
        Long userId = this.authService.login(loginUserRequestDto);

        // 세션 저장
        SessionUserUtils.setId(userId, request);

        return ResponseEntity
                .ok()
                .body(new AuthMessageResponseDto("로그인에 성공하였습니다."));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthMessageResponseDto> logout(
            HttpServletRequest request
    ) {
       SessionUserUtils.invalidate(request);

        return ResponseEntity
                .ok()
                .body(new AuthMessageResponseDto("로그아웃 되었습니다."));
    }
}
