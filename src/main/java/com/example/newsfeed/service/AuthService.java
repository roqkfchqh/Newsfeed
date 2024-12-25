package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.AuthMapper;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.template.AuthAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService extends AuthAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    protected SignupUserResponseDto executeSignup(SignupUserRequestDto signupUserRequestDto) {
        User user = AuthMapper.fromSignupUserRequestDto(
                signupUserRequestDto,
                encoder.encode(signupUserRequestDto.getPassword())
        );
        User savedUser = this.userRepository.save(user);
        return AuthMapper.toSignupUserResponseDto(savedUser);
    }

    @Override
    protected Long executeLogin(Long userId) {
        return getNotDeletedUserById(userId).getId();
    }

    // validator
    @Override
    protected void validateExistUserEmail(String email) {
        Optional<User> checkUser = this.userRepository.findByEmail(email);

        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }

    @Override
    protected void validateUserPassword(Long userId, String currentPassword) {
        String findHashedPassword = getUserById(userId).getPassword();

        if (!encoder.matches(currentPassword, findHashedPassword)) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    @Override
    protected Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));

        return user.getId();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private User getNotDeletedUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
