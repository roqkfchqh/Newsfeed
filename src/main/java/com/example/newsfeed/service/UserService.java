package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.UserMapper;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.UserAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService extends UserAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    @Transactional
    public UpdateUserNameResponseDto executeUpdateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto) {
        User user = getUserById(userId);
        user.updateUserName(updateUserReqDto.getName());
        return UserMapper.toUpdateUserNameResponseDto(user);
    }

    @Override
    public FetchUserResponseDto executeFetchOneById(Long userId) {
        User user = getUserById(userId);
        return UserMapper.toFetchUserResponseDto(user);
    }

    @Override
    @Transactional
    public void executeUpdateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        String updatedHashedPassword = encoder.encode(updateUserPasswordRequestDto.getUpdatePassword());
        User user = getUserById(userId);
        user.updatePassword(updatedHashedPassword);
    }

    @Override
    @Transactional
    public void executeSoftDeleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        User user = getUserById(userId);
        user.softDelete();
    }

    // validator
    @Override
    public void validateUserPassword(Long userId, String currentPassword) {
        String findHashedPassword = getUserById(userId).getPassword();

        if (!encoder.matches(currentPassword, findHashedPassword)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
