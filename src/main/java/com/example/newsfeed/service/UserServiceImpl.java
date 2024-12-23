package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends UserAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public CreateUserResponseDto executeCreateUser(CreateUserRequestDto createUserRequestDto) {
        User user = User.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(encoder.encode(createUserRequestDto.getPassword()))
                .build();

        User savedUser = this.userRepository.save(user);
        return CreateUserResponseDto.of(savedUser);
    }

    @Override
    @Transactional
    public UpdateUserNameResponseDto executeUpdateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto) {
        User user = getUserById(userId);
        user.updateUserName(updateUserReqDto.getName());
        return UpdateUserNameResponseDto.of(user);
    }

    @Override
    public FetchUserResponseDto executeFetchOneById(Long userId) {
        User user = getUserById(userId);
        return FetchUserResponseDto.of(user);
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
    public void executeDeleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        User user = getUserById(userId);
        user.softDelete();
    }

    // validator

    @Override
    public void validateUserEmail(String email) {
        Optional<User> checkUser = this.userRepository.findByEmail(email);

        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }

    @Override
    public void validateUserPassword(Long userId, String currentPassword) {
        String findHashedPassword = getUserById(userId).getPassword();

        if (!encoder.matches(currentPassword, findHashedPassword)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }
    }

    //

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
