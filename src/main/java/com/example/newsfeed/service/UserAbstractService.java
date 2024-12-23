package com.example.newsfeed.service;

import com.example.newsfeed.dto.user.*;

public abstract class UserAbstractService {

    /**
     * 유저 생성
     */
    public final CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        validateUserEmail(createUserRequestDto.getEmail());
        return executeCreateUser(createUserRequestDto);
    }

    /**
     * 유저 이름 갱신
     */
    public final UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserNameRequestDto) {
        return executeUpdateUserName(userId, updateUserNameRequestDto);
    }

    /**
     * 유저 조회
     */
    public final FetchUserResponseDto fetchOneById(Long userId) {
        return executeFetchOneById(userId);
    }

    /**
     * 유저 비밀번호 갱신
     */
    public final void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        validateUserPassword(userId, updateUserPasswordRequestDto.getCurrentPassword());
        executeUpdateUserPassword(userId, updateUserPasswordRequestDto);
    }

    /**
     * 유저 삭제
     */
    public final void deleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        validateUserPassword(userId, deleteUserRequestDto.getCurrentPassword());
        executeDeleteUser(userId, deleteUserRequestDto);
    }

    // validator
    protected abstract void validateUserEmail(String email);
    protected abstract void validateUserPassword(Long userId, String currentPassword);


    // business logic
    protected abstract CreateUserResponseDto executeCreateUser(CreateUserRequestDto createUserRequestDto);
    protected abstract UpdateUserNameResponseDto executeUpdateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto);
    protected abstract void executeUpdateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto);
    protected abstract FetchUserResponseDto executeFetchOneById(Long userId);
    protected abstract void executeDeleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto);
}
