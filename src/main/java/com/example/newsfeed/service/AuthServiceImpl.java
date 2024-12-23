package com.example.newsfeed.service;

import com.example.newsfeed.bcrypt.Encoder;
import com.example.newsfeed.dto.auth.LoginUserRequestDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    @Override
    public Long login(LoginUserRequestDto loginUserRequestDto) {
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
}
