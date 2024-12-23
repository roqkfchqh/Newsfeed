package com.example.newsfeed.dto.friend;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FriendResponseDto {

    private Long id;
    private Long followerId;
    private Long followeeId;
    private Boolean followed;
}
