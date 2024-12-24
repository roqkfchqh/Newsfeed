package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService extends AuthAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public Long executeLogin(Long userId) {
        return getUserById(userId).getId();
    }

    @Override
    public CreateUserResponseDto executeSignup(CreateUserRequestDto createUserRequestDto) {
        User user = User.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(encoder.encode(createUserRequestDto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return CreateUserResponseDto.of(savedUser);
    }

    // validator
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

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
