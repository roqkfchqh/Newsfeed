package com.example.newsfeed.service;

import com.example.newsfeed.dto.user.*;

public interface UserService {

    /**
     * 유저 생성
     */
    CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    /**
     * 유저 이름 갱신
     */
    UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto);

    /**
     * 유저 비밀번호 갱신
     */
    void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto);

    /**
     * 유저 조회
     */
    FetchUserResponseDto fetchOneById(Long userId);

    /**
     * 유저 삭제
     */
    void deleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto);
}
