package com.example.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    /*
    [Posts]
    //session required
    게시물 생성 POST /posts

    게시물 조회 GET /posts

    //session required
    게시물 갱신 PATCH /posts/{postId}

    //session required
    //좋아요 갱신 POST /posts/{postId}/likes
    //response: 성공 메세지

    //session required
    //좋아요 갱신 DELETE /posts/{postId}/likes
    //response: 삭제 성공 메세지

    //session required
    게시물 삭제 DELETE /posts/{postId}

     */
}
