package com.example.newsfeed.dto.friend;

import lombok.Builder;

@Builder
public class FriendResponseDto {

    private Long id;
    private String followerName;
    private Long followerId;
    private String followeeName;
    private Long followeeId;
    private Boolean isFriend;
}
