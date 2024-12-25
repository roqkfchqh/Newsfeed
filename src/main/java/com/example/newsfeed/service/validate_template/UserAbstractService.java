package com.example.newsfeed.service.validate_template;

import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.model.User;

public abstract class UserAbstractService {

    // service logic
    /**
     * 유저 이름 갱신
     */
    public final UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserNameRequestDto) {
        User user = getNotDeletedUserById(userId);
        return executeUpdateUserName(user, updateUserNameRequestDto.getName());
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
        User user = getNotDeletedUserById(userId);
        validateUserPassword(user, updateUserPasswordRequestDto.getCurrentPassword());
        executeUpdateUserPassword(user, updateUserPasswordRequestDto.getUpdatePassword());
    }

    /**
     * 유저 삭제
     */
    public final void softDeleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        User user = getNotDeletedUserById(userId);
        validateUserPassword(user, deleteUserRequestDto.getCurrentPassword());
        executeSoftDeleteUser(user);
    }

    // getter
    protected abstract User getNotDeletedUserById(Long userId);

    // validator
    protected abstract void validateUserPassword(User user, String currentPassword);

    // business logic
    protected abstract UpdateUserNameResponseDto executeUpdateUserName(User user, String name);
    protected abstract void executeUpdateUserPassword(User user, String updatedPassword);
    protected abstract FetchUserResponseDto executeFetchOneById(Long userId);
    protected abstract void executeSoftDeleteUser(User user);
}
