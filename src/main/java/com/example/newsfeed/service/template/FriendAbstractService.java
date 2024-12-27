package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;

import java.util.List;

public abstract class FriendAbstractService {

    // Create a friend request
    public final FriendResponseDto createFriend(Long friendId, Long userId) {
        validateSelfRequest(friendId, userId);
        if (validateRelation(friendId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND);
        }
        validateFollowExists(friendId, userId);
        return executeCreateFriend(friendId, userId);
    }

    // Accept a friend request
    public final void acceptFriend(Long relationId, Long userId) {
        if (validateRelation(relationId)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND);
        }
        validateAuthority(relationId, userId);
        executeAcceptFriend(relationId, userId);
    }

    // Reject a friend request
    public final void rejectFriend(Long relationId, Long userId) {
        if (validateRelation(relationId)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND);
        }
        validateAuthority(relationId, userId);
        executeRejectFriend(relationId, userId);
    }

    // Get the list of followers who follow the user
    public final List<FriendResponseDto> getFollowers(Long userId) {
        return executeGetFollowers(userId);
    }

    // Get the list of followees who the user follows
    public final List<FriendResponseDto> getFollowees(Long userId) {
        return executeGetFollowees(userId);
    }

    // Get the list of friends
    public final List<FriendResponseDto> getFriends(Long userId) {
        return executeGetFriends(userId);
    }

    // Delete a friend relationship
    public void deleteFriend(Long relationId, Long userId) {
        if (!validateRelation(relationId)) {
            throw new CustomException(ErrorCode.ALREADY_NOT_FRIEND);
        }
        validateDelete(relationId, userId);
        executeDeleteFriend(relationId, userId);
    }


    //validator
    protected abstract Boolean validateRelation(Long friendId, Long userId);

    protected abstract Boolean validateRelation(Long relationId); //관계 검증(true = 친구 false = 팔로잉 팔로워 관계)

    protected abstract void validateFollowExists(Long friendId, Long userId);

    protected abstract void validateAuthority(Long relationId, Long userId);    //수락 / 거절권한 확인

    protected abstract void validateDelete(Long relationId, Long userId);   //삭제권한 확인

    protected abstract void validateSelfRequest(Long friendId, Long userId);

    // business logic
    protected abstract FriendResponseDto executeCreateFriend(Long friendId, Long userId);

    protected abstract void executeAcceptFriend(Long relationId, Long userId);

    protected abstract void executeRejectFriend(Long relationId, Long userId);

    protected abstract List<FriendResponseDto> executeGetFollowers(Long userId);

    protected abstract List<FriendResponseDto> executeGetFollowees(Long userId);

    protected abstract List<FriendResponseDto> executeGetFriends(Long userId);

    protected abstract void executeDeleteFriend(Long relationId, Long userId);
}
