package com.example.newsfeed.dto.friend;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRequestDto {

    @NotBlank(message = "팔로우할 사용자의 ID를 적어주십시오.")
    private Long followeeId;
}
