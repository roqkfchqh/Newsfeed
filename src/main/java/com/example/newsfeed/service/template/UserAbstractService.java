package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.model.User;

public abstract class UserAbstractService {

    // Update the user's name
    public final UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserNameRequestDto) {
        return executeUpdateUserName(userId, updateUserNameRequestDto.getName());
    }

    // Fetch a user by their ID
    public final FetchUserResponseDto fetchOneById(Long userId) {
        return executeFetchOneById(userId);
    }

    // Update the user's password
    public final void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        validateUserPassword(userId, updateUserPasswordRequestDto.getCurrentPassword());
        executeUpdateUserPassword(userId, updateUserPasswordRequestDto.getUpdatePassword());
    }

    // Soft delete a user (deactivation)
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
