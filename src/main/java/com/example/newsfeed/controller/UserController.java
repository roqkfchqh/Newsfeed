package com.example.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    //create POST /users
    //request: email, name, password
    //response: email, name, cratedAt

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
