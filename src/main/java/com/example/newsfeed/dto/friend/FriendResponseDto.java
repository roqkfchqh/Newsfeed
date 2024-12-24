package com.example.newsfeed.dto.friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponseDto {

    private Long id;
    private String followerName;
    private Long followerId;
    private String followeeName;
    private Long followeeId;
    private Boolean isFriend;
}
