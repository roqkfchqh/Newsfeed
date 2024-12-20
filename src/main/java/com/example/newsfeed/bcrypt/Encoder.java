package com.example.newsfeed.bcrypt;

public interface Encoder {
    /**
     * 비밀번호 암호화
     */
    String encode(String rawPassword);

    /**
     * 평문 비밀번호와 암호화된 비밀번호 비교
     */
    boolean matches(String rawPassword, String encodedPassword);
}
