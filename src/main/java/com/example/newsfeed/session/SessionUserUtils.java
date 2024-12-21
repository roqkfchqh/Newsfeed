package com.example.newsfeed.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUserUtils {

    /**
     * 세션으로부터 로그인된 유저의 ID 데이터를 받음
     */
    public static Long getId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (Long) session.getAttribute(SessionTags.LOGIN_USER.getTag());
    }

    /**
     * 세션에 로그인한 유저의 ID 데이터를 저장
     */
    public static void setId(Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionTags.LOGIN_USER.getTag(), id);
    }

    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
    }
}
