package com.example.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    //login
    //request: email, password
    //response -> 완료

    //session required
    //logout -> session 비활성화
    //response -> 완료
}
