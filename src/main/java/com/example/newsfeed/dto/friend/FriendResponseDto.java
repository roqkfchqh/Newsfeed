package com.example.newsfeed.dto.friend;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FriendResponseDto {

    private Long id;
    private Long followerId;    //string name, email
    private Long followeeId;    //string name, email
    private Boolean followed; // isFriend
}
