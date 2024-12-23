package com.example.newsfeed.dto.friend;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FriendRequestDto {

    @NotNull(message = "팔로우할 사용자의 ID를 적어주십시오.")
    private Long followeeId;
}
