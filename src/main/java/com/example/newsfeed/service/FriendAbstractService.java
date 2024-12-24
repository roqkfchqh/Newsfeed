package com.example.newsfeed.service;

import com.example.newsfeed.dto.friend.FriendRequestDto;
import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;

import java.util.List;

public abstract class FriendAbstractService {
    /*
    채영 : validator 이름 userValidatorForFriend, friendValidatorForFriend 이런식으로 하시면 됩니다.
     */

    //요청
    public final FriendResponseDto createFriend(FriendRequestDto requestDto, Long userId) {
        userValidatorForFriend(userId);
        friendValidatorForFriend(requestDto.getFolloweeId());
        return executeCreateFriend(requestDto.getFolloweeId(), userId);
    }

    //수락
    public final FriendResponseDto acceptFriend(FriendRequestDto requestDto, Long userId) {
        userValidatorForFriend(userId);
        friendValidatorForFriend(requestDto.getFolloweeId());
        if(relationValidatorForFriend(requestDto.getFolloweeId(), userId)) {    //true ( 이미 친구)
            throw new CustomException(ErrorCode.ALREADY_LIKED); //이따 바꿀거임
        }
        authorityValidatorForFriend(requestDto.getFolloweeId(), userId);
        return executeAcceptFriend(requestDto.getFolloweeId(), userId);
    }

    //거절
    public final FriendResponseDto rejectFriend(FriendRequestDto requestDto, Long userId) {
        userValidatorForFriend(userId);
        friendValidatorForFriend(requestDto.getFolloweeId());
        if(relationValidatorForFriend(requestDto.getFolloweeId(), userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED); //이따 바꿀거임
        }
        authorityValidatorForFriend(requestDto.getFolloweeId(), userId);
        return executeRejectFriend(requestDto.getFolloweeId(), userId);
    }

    public final List<FriendResponseDto> getFollowers(Long userId) {
        userValidatorForFriend(userId);
        return null;
    }

    public final List<FriendResponseDto> getFollowees(Long userId) {
        userValidatorForFriend(userId);
        return null;
    }

    public final List<FriendResponseDto> getFriends(Long userId) {
        userValidatorForFriend(userId);
        return null;
    }

    public void deleteFriend(Long friendId, Long userId) {
        userValidatorForFriend(userId);
        friendValidatorForFriend(friendId);
    }

    protected abstract void userValidatorForFriend(Long userId);    //유저검증
    protected abstract void friendValidatorForFriend(Long friendId);    //상대방검증

    protected abstract Boolean relationValidatorForFriend(Long friendId, Long userId); //관계 검증(true = 친구 false = 팔로잉 팔로워 관계)
    protected abstract void authorityValidatorForFriend(Long friendId, Long userId);    //권한 확인


    protected abstract FriendResponseDto executeCreateFriend(Long friendId, Long userId);
    protected abstract FriendResponseDto executeAcceptFriend(Long friendId, Long userId);
    protected abstract FriendResponseDto executeRejectFriend(Long friendId, Long userId);



}
