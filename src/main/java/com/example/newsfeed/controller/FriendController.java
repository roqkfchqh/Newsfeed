package com.example.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    //follower < 나를 팔로우 하는 사람들(팔로워)
    //followee < 내가 팔로우 하는 사람들(구독)

    //session required
    //create POST /friends
    //request: followee
    //response: status, followee

    //session required
    //update PATCH /friends/{friendId}
    //response: status, follower, followee

    //session required
    //read GET /friends/follower
    //response: List<follower>

    //session required
    //read GET /friends/followee
    //response: List<followee>

    //session required
    //delete DELETE /friends/{friendId}
    //response: 삭제 성공 메세지
}
