package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.model.User;

public abstract class UserAbstractService {

    // service logic
    /**
     * 유저 이름 갱신
     */
    public final UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserNameRequestDto) {
        return executeUpdateUserName(userId, updateUserNameRequestDto.getName());
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
        executeUpdateUserPassword(userId, updateUserPasswordRequestDto.getUpdatePassword());
    }

    /**
     * 유저 삭제
     */
    public final void softDeleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        validateUserPassword(userId, deleteUserRequestDto.getCurrentPassword());
        executeSoftDeleteUser(userId);
    }

    // getter
    protected abstract User validateUserId(Long userId);

    // validator
    protected abstract void validateUserPassword(Long userId, String currentPassword);

    // business logic
    protected abstract UpdateUserNameResponseDto executeUpdateUserName(Long userId, String updateName);
    protected abstract void executeUpdateUserPassword(Long userId, String updatedPassword);
    protected abstract FetchUserResponseDto executeFetchOneById(Long userId);
    protected abstract void executeSoftDeleteUser(Long userId);
}
