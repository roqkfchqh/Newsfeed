package com.example.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    //session required
    //create POST /comments
    //request: content
    //response: username, content, createdAt

    //session required
    //update PATCH /comments/{commentId}
    //request: updateContents
    //response: content, updatedAt

    //read GET /comments/{commentId} <- post 에 통합?
    //response: username, content, cratedAt, updatedAt

    //session required
    //좋아요 갱신 POST /comments/{commentId}/likes
    //response: 성공 메세지

    //session required
    //좋아요 갱신 DELETE /comments/{commentId}/likes
    //response: 삭제 성공 메세지

    //session required
    //delete DELETE /comments/{commentId}
    //response: 삭제 성공 메세지
}
