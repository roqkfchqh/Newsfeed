package com.example.newsfeed.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SessionTags {
    LOGIN_USER("login_user");

    private final String tag;
}
