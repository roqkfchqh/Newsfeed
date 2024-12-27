package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.user.FetchUserResponseDto;
import com.example.newsfeed.dto.user.UpdateUserNameResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.UserMapper;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.template.UserAbstractService;
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
    protected UpdateUserNameResponseDto executeUpdateUserName(Long userId, String updateName) {
        User user = getUserById(userId);
        user.updateUserName(updateName);
        return UserMapper.toUpdateUserNameResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    protected FetchUserResponseDto executeFetchOneById(Long userId) {
        User user = validateUserId(userId);
        return UserMapper.toFetchUserResponseDto(user);
    }

    @Override
    @Transactional
    protected void executeUpdateUserPassword(Long userId, String updatedPassword) {
        User user = getUserById(userId);
        String updatedHashedPassword = encoder.encode(updatedPassword);
        user.updatePassword(updatedHashedPassword);
    }

    @Override
    protected void executeSoftDeleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.deleteById(user.getId());
    }

    // validator
    @Override
    protected void validateUserPassword(Long userId, String currentPassword) {
        User user = getUserById(userId);
        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Override
    protected User validateUserId(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
