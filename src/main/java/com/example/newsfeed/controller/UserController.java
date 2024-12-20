package com.example.newsfeed.controller;

import com.example.newsfeed.dto.user.CreateUserRequestDto;
import com.example.newsfeed.dto.user.CreateUserResponseDto;
import com.example.newsfeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //create POST /users
    //request: email, name, password
    //response: email, name, cratedAt
    @PostMapping
    public ResponseEntity<CreateUserResponseDto> createUser(
        @RequestBody CreateUserRequestDto createUserRequestDto
    ) {
        CreateUserResponseDto data = this.userService.createUser(createUserRequestDto);

        return ResponseEntity
                .ok()
                .body(data);
    }

    //session required
    //update PATCH /users
    //request: updateName
    //response: updateName, updatedAt

    //session required
    //update PATCH /users/password
    //request: currentPassword, updatePassword
    //response: 수정 완료 메세지

    //read GET /users/{userId}
    //response: email, name, createdAt, updatedAt

    //session required
    //delete DELETE /users
    //request: currentPassword
    //response: 삭제 성공 메시지
}
