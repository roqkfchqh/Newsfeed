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
     * 유저(본인) 조회
     */
    @GetMapping
    public ResponseEntity<BaseResponseDto<FetchUserResponseDto>> getCurrentUser(
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        FetchUserResponseDto data = this.userService.fetchOneById(userId);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저(타인) 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponseDto<FetchUserResponseDto>> getUser(
            @PathVariable Long userId
    ) {
        FetchUserResponseDto data = this.userService.fetchOneById(userId);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저 이름 수정
     */
    @PatchMapping
    public ResponseEntity<BaseResponseDto<UpdateUserNameResponseDto>> executeUpdateUserName(
            @Valid @RequestBody UpdateUserNameRequestDto updateUserReqDto,
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        UpdateUserNameResponseDto data = this.userService.updateUserName(userId, updateUserReqDto);

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저 비밀번호 수정
     */
    @PatchMapping("/password")
    public ResponseEntity<BaseResponseDto<UserMessageResponseDto>> executeUpdateUserPassword(
            @Valid @RequestBody UpdateUserPasswordRequestDto updateUserPasswordRequestDto,
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        this.userService.updateUserPassword(userId, updateUserPasswordRequestDto);
        UserMessageResponseDto data = new UserMessageResponseDto("비밀번호가 성공적으로 수정되었습니다.");

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }

    /**
     * 유저 삭제(회원 탈퇴)
     */
    @DeleteMapping
    public ResponseEntity<BaseResponseDto<UserMessageResponseDto>> executeSoftDeleteUser(
            @Valid @RequestBody DeleteUserRequestDto deleteUserRequestDto,
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        this.userService.softDeleteUser(userId, deleteUserRequestDto);
        SessionUserUtils.invalidate(request);
        UserMessageResponseDto data = new UserMessageResponseDto("사용자가 성공적으로 삭제되었습니다.");

        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(data));
    }
}
