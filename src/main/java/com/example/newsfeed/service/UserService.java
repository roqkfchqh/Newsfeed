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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Encoder encoder;

    /**
     * 유저 생성
     */
    @Transactional
    public CreateUserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        // 중복 이메일 검증
        Optional<User> checkUser = this.userRepository.findByEmail(createUserRequestDto.getEmail());

        // 유저가 없어야 로직 수행함 -> isPresent()로 null check
        if (checkUser.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_EMAIL);
        }

        // 암호화 후 유저 엔티티 생성
        User user = User.builder()
                .name(createUserRequestDto.getName())
                .email(createUserRequestDto.getEmail())
                .password(encoder.encode(createUserRequestDto.getPassword()))
                .build();

        User savedUser = this.userRepository.save(user);

        return CreateUserResponseDto.of(savedUser);
    }

    /**
     * 유저 이름 갱신
     */
    @Transactional
    public UpdateUserNameResponseDto updateUserName(Long userId, UpdateUserNameRequestDto updateUserReqDto) {
        // 검사
        User findUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 수정
        findUser.setName(updateUserReqDto.getName());

        return UpdateUserNameResponseDto.of(findUser);
    }

    /**
     * 유저 비밀번호 갱신
     */
    @Transactional
    public void updateUserPassword(Long userId, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        // 검사
        User findUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String findHashedEmail = findUser.getPassword();

        // 입력받은 현재 비밀번호와 암호화 된 비밀번호 비교
        if (!this.encoder.matches(updateUserPasswordRequestDto.getCurrentPassword(), findHashedEmail)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        // 비밀번호 수정
        String updatedHashedPassword = this.encoder.encode(updateUserPasswordRequestDto.getUpdatePassword());
        findUser.setPassword(updatedHashedPassword);
    }

    /**
     * 유저 조회
     */
    public FetchUserResponseDto fetchOneById(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return FetchUserResponseDto.of(user);
    }

    /**
     * 유저 삭제
     */
    @Transactional
    public void deleteUser(Long userId, DeleteUserRequestDto deleteUserRequestDto) {
        User findUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String findHashedEmail = findUser.getPassword();

        // 입력받은 현재 비밀번호와 암호화 된 비밀번호 비교
        if (!this.encoder.matches(deleteUserRequestDto.getCurrentPassword(), findHashedEmail)) {
            throw new CustomException(ErrorCode.WRONG_PASSWORD);
        }

        this.userRepository.delete(findUser);
    }
}
