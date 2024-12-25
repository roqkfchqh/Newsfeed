package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.AuthMapper;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.AuthAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService extends AuthAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public SignupUserResponseDto executeSignup(SignupUserRequestDto signupUserRequestDto) {
        User user = AuthMapper.fromSignupUserRequestDto(
                signupUserRequestDto,
                encoder.encode(signupUserRequestDto.getPassword())
        );
        User savedUser = this.userRepository.save(user);
        return AuthMapper.toSignupUserResponseDto(savedUser);
    }

    @Override
    public Long executeLogin(Long userId) {
        return getNotDeletedUserById(userId).getId();
    }

    // validator
    @Override
    public void validateExistUserEmail(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }

    @Override
    public void validateUserPassword(String currentPassword, String hashedPassword) {
        if (!encoder.matches(currentPassword, hashedPassword)) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNotNull(email)
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));
    }

    private User getNotDeletedUserById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
