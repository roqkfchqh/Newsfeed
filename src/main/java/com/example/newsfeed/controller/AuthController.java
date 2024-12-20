package com.example.newsfeed.controller;

import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.service.AuthService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    //login
    //request: email, password
    //response -> 완료
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginUserRequestDto loginUserRequestDto,
            HttpServletRequest request
    ) {
        // 검증을 위한 서비스 호출
        Long userId = this.authService.login(loginUserRequestDto);

        // 세션 저장
        SessionUserUtils.setId(userId, request);

        return ResponseEntity
                .ok("로그인 성공");
    }

    //session required
    //logout -> session 비활성화
    //response -> 완료
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity
                .ok("로그인 아웃");
    }
}
