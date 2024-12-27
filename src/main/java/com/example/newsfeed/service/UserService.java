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
    protected UpdateUserNameResponseDto executeUpdateUserName(User user, String name) {
        user.updateUserName(name);
        return UserMapper.toUpdateUserNameResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    protected FetchUserResponseDto executeFetchOneById(Long userId) {
        User user = getNotDeletedUserById(userId);
        return UserMapper.toFetchUserResponseDto(user);
    }

    @Override
    @Transactional
    protected void executeUpdateUserPassword(User user, String updatedPassword) {
        String updatedHashedPassword = encoder.encode(updatedPassword);
        user.updatePassword(updatedHashedPassword);
    }

    @Override
    @Transactional
    protected void executeSoftDeleteUser(User user) {
        userRepository.deleteById(user.getId());
    }

    // validator
    @Override
    protected void validateUserPassword(User user, String currentPassword) {
        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Override
    protected User getNotDeletedUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
