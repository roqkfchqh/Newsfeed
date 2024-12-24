package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.service.UserService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 생성(회원 가입)
     */
    @PostMapping
    public ResponseEntity<BaseResponseDto<CreateUserResponseDto>> createUser(
        @Valid @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        CreateUserResponseDto data = this.userService.createUser(createUserRequestDto);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저(본인) 조회
     */
    @GetMapping
    public ResponseEntity<BaseResponseDto<FetchUserResponseDto>> fetchUser(
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        FetchUserResponseDto data = this.userService.fetchOneById(userId);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저 이름 수정
     */
    @PatchMapping
    public ResponseEntity<BaseResponseDto<UpdateUserNameResponseDto>> updateUserName(
            @Valid @RequestBody UpdateUserNameRequestDto updateUserReqDto,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        UpdateUserNameResponseDto data = this.userService.updateUserName(userId, updateUserReqDto);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저 비밀번호 수정
     */
    @PatchMapping("/password")
    public ResponseEntity<UserMessageResponseDto> updateUserPassword(
            @Valid @RequestBody UpdateUserPasswordRequestDto updateUserPasswordRequestDto,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        this.userService.updateUserPassword(userId, updateUserPasswordRequestDto);

        return ResponseEntity
                .ok()
                .body(new UserMessageResponseDto("비밀번호가 성공적으로 수정되었습니다."));
    }

    /**
     * 유저 삭제(회원 탈퇴)
     */
    @DeleteMapping
    public ResponseEntity<UserMessageResponseDto> deleteUser(
            @Valid @RequestBody DeleteUserRequestDto deleteUserRequestDto,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);

        this.userService.deleteUser(userId, deleteUserRequestDto);

        SessionUserUtils.invalidate(request);

        return ResponseEntity
                .ok()
                .body(new UserMessageResponseDto("사용자가 성공적으로 삭제되었습니다."));
    }

    /*
    helper method
     */
    private static Long getUserId(HttpServletRequest request) {
        return SessionUserUtils.getId(request);
    }
}
