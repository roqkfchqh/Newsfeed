package com.example.newsfeed.service.validate_template;

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
        validateUser(userId);
        validateFriend(requestDto.getFolloweeId());
        return executeCreateFriend(requestDto.getFolloweeId(), userId);
    }

    //수락
    public final FriendResponseDto acceptFriend(FriendRequestDto requestDto, Long userId) {
        validateUser(userId);
        validateFriend(requestDto.getFolloweeId());
        if(validateRelation(requestDto.getFolloweeId(), userId)) {    //true ( 이미 친구)
            throw new CustomException(ErrorCode.ALREADY_LIKED); //이따 바꿀거임
        }
        validateAuthority(requestDto.getFolloweeId(), userId);
        return executeAcceptFriend(requestDto.getFolloweeId(), userId);
    }

    //거절
    public final FriendResponseDto rejectFriend(FriendRequestDto requestDto, Long userId) {
        validateUser(userId);
        validateFriend(requestDto.getFolloweeId());
        if(validateRelation(requestDto.getFolloweeId(), userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED); //이따 바꿀거임
        }
        validateAuthority(requestDto.getFolloweeId(), userId);
        return executeRejectFriend(requestDto.getFolloweeId(), userId);
    }

    public final List<FriendResponseDto> getFollowers(Long userId) {
        validateUser(userId);
        return null;
    }

    public final List<FriendResponseDto> getFollowees(Long userId) {
        validateUser(userId);
        return null;
    }

    public final List<FriendResponseDto> getFriends(Long userId) {
        validateUser(userId);
        return null;
    }

    public void deleteFriend(Long friendId, Long userId) {
        validateUser(userId);
        validateFriend(friendId);
    }

    /*
    validator
     */

    protected abstract void validateUser(Long userId);    //유저검증
    protected abstract void validateFriend(Long friendId);    //상대방검증

    protected abstract Boolean validateRelation(Long friendId, Long userId); //관계 검증(true = 친구 false = 팔로잉 팔로워 관계)
    protected abstract void validateAuthority(Long friendId, Long userId);    //권한 확인

    /*
    business logic
     */

    protected abstract FriendResponseDto executeCreateFriend(Long friendId, Long userId);
    protected abstract FriendResponseDto executeAcceptFriend(Long friendId, Long userId);
    protected abstract FriendResponseDto executeRejectFriend(Long friendId, Long userId);



}
