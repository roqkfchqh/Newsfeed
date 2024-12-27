package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.user.*;
import com.example.newsfeed.mapper.BaseResponseMapper;
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

    // Get current user (GET /users)
    @GetMapping
    public ResponseEntity<BaseResponseDto<FetchUserResponseDto>> getCurrentUser(HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        FetchUserResponseDto data = userService.fetchOneById(userId);
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Get specific user (GET /users/{userId})
    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponseDto<FetchUserResponseDto>> getUser(@PathVariable Long userId) {
        FetchUserResponseDto data = userService.fetchOneById(userId);
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Update user name (PATCH /users)
    @PatchMapping
    public ResponseEntity<BaseResponseDto<UpdateUserNameResponseDto>> executeUpdateUserName(@Valid @RequestBody UpdateUserNameRequestDto updateUserReqDto, HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        UpdateUserNameResponseDto data = userService.updateUserName(userId, updateUserReqDto);
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Update user password (PATCH /users/password)
    @PatchMapping("/password")
    public ResponseEntity<BaseResponseDto<UserMessageResponseDto>> executeUpdateUserPassword(@Valid @RequestBody UpdateUserPasswordRequestDto updateUserPasswordRequestDto, HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        userService.updateUserPassword(userId, updateUserPasswordRequestDto);
        UserMessageResponseDto data = new UserMessageResponseDto("비밀번호가 성공적으로 수정되었습니다.");
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }

    // Delete user (Account Deletion) (DELETE /users)
    @DeleteMapping
    public ResponseEntity<BaseResponseDto<UserMessageResponseDto>> executeSoftDeleteUser(@Valid @RequestBody DeleteUserRequestDto deleteUserRequestDto, HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        userService.softDeleteUser(userId, deleteUserRequestDto);
        SessionUserUtils.invalidate(request);
        UserMessageResponseDto data = new UserMessageResponseDto("사용자가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok().body(BaseResponseMapper.map(data));
    }
}
