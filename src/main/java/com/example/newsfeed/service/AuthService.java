package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.SignupUserRequestDto;
import com.example.newsfeed.dto.auth.SignupUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.AuthMapper;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService extends AuthAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public SignupUserResponseDto executeSignup(SignupUserRequestDto signupUserRequestDto) {
        User user = AuthMapper.toEntity(
                signupUserRequestDto,
                encoder.encode(signupUserRequestDto.getPassword())
        );
        User savedUser = this.userRepository.save(user);
        return AuthMapper.toDto(savedUser);
    }

    @Override
    public Long executeLogin(Long userId) {
        return getUserById(userId).getId();
    }

    /*
    validator
     */

    @Override
    public void validateExistUserEmail(String email) {
        Optional<User> checkUser = this.userRepository.findByEmail(email);

        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }
    }

    @Override
    public void validateUserPassword(Long userId, String currentPassword) {
        String findHashedPassword = getUserById(userId).getPassword();

        if (!encoder.matches(currentPassword, findHashedPassword)) {
            throw new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        }
    }

    @Override
    public Long getUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));

        return user.getId();
    }

    /*
    helper method
     */

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
