package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.model.User;

public abstract class UserAbstractService {

    // Update the user's name
    public final UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserNameRequestDto) {
        User user = getNotDeletedUserById(userId);
        return executeUpdateUserName(user, updateUserNameRequestDto.getName());
    }

    // Fetch a user by their ID
    public final FetchUserResponseDto fetchOneById(Long userId) {
        return executeFetchOneById(userId);
    }

    // Update the user's password
    public final void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        User user = getNotDeletedUserById(userId);
        validateUserPassword(user, updateUserPasswordRequestDto.getCurrentPassword());
        executeUpdateUserPassword(user, updateUserPasswordRequestDto.getUpdatePassword());
    }

    // Soft delete a user (deactivation)
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
