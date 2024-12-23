package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthAbstractService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public Long executeLogin(LoginUserRequestDto loginUserRequestDto) {
        User user = getUserById(loginUserRequestDto.getEmail());

        // 조회
        Optional<User> checkUser = this.userRepository.findByEmail(loginUserRequestDto.getEmail());

        // 이메일 검증
        if (checkUser.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User findUser = checkUser.get();
        String findHashedEmail = findUser.getPassword();

        if (!this.encoder.matches(loginUserRequestDto.getPassword(), findHashedEmail)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }


        return findUser.getId();
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
    public void validateUserEmail(String email) {
        Optional<User> checkUser = userRepository.findByEmail(email);

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

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private User getUserById(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
