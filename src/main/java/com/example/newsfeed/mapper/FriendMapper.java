package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.model.Friend;

public class FriendMapper {

    public static FriendResponseDto toDto(Friend friend) {
        return FriendResponseDto.builder()

                .build();
    }

    public static Friend toEntity(FriendResponseDto friendResponseDto) {
        return Friend.builder()

                .build();
    }
}
