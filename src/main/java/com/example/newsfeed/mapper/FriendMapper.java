package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.model.Friend;

public class FriendMapper {

    public static FriendResponseDto toFriendResponseDto(Friend friend) {
        return FriendResponseDto.builder()

                .build();
    }

    public static Friend fromFriendResponseDto(FriendResponseDto friendResponseDto) {
        return Friend.builder()

                .build();
    }
}
