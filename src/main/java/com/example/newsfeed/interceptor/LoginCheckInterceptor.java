package com.example.newsfeed.interceptor;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.session.SessionTags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    private final String[] uriWhiteList = {
            "/users", // 유저 가입
            "/auth/login", // 로그인, 로그아웃
    };

    private final String[] methodWhiteList = {
            "POST",
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (PatternMatchUtils.simpleMatch(uriWhiteList, request.getRequestURI())
                && PatternMatchUtils.simpleMatch(methodWhiteList, request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionTags.LOGIN_USER.getTag()) == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        return true;
    }
}
