package com.example.newsfeed.mapper;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.model.Friend;
import com.example.newsfeed.model.User;

public class FriendMapper {

    public static FriendResponseDto toDto(Friend friend) {
        if (friend == null || friend.getFollower() == null || friend.getFollowee() == null) {
            throw new IllegalArgumentException("Friend or associated user is null");
        }
        return FriendResponseDto.builder()
                .id(friend.getId())
                .followerName(friend.getFollower().getName())
                .followerId(friend.getFollower().getId())
                .followeeName(friend.getFollowee().getName())
                .followeeId(friend.getFollowee().getId())
                .isFriend(friend.getFollow())
                .build();
    }

    public static Friend toEntity(User user, User friend) {
        return Friend.builder()
                .follower(user)
                .followee(friend)
                .follow(false)
                .build();
    }
}
