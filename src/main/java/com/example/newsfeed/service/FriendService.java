package com.example.newsfeed.service;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.FriendMapper;
import com.example.newsfeed.model.Friend;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.template.FriendAbstractService;
import com.example.newsfeed.service.validate.ValidateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService extends FriendAbstractService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final ValidateHelper validateHelper;

    @Override
    protected FriendResponseDto executeCreateFriend(Long friendId, Long userId){
        validateHelper.user(userId);
        validateHelper.user(friendId);

        User user = getUser(userId);
        User friend = getUser(friendId);

        Friend relate = FriendMapper.toEntity(user, friend);

        Friend savedFriend = friendRepository.save(relate);
        return FriendMapper.toDto(savedFriend);
    }

    @Transactional
    @Override
    protected void executeAcceptFriend(Long relationId, Long userId) {
        validateHelper.user(userId);
        validateHelper.friend(relationId);

        Friend friend = getFriend(relationId);
        friend.acceptFollow();
    }

    @Override
    protected void executeRejectFriend(Long relationId, Long userId) {
        validateHelper.user(userId);
        validateHelper.friend(relationId);

        Friend friend = getFriend(relationId);
        friendRepository.delete(friend);
    }

    @Override
    protected List<FriendResponseDto> executeGetFollowers(Long userId){
        validateHelper.user(userId);

        List<Friend> followers = friendRepository.findByFollower(userId);
        return followers.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    protected List<FriendResponseDto> executeGetFollowees(Long userId) {
        validateHelper.user(userId);

        List<Friend> followees = friendRepository.findByFollowee(userId); //
        return followees.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    protected List<FriendResponseDto> executeGetFriends(Long userId) {
        validateHelper.user(userId);

        List<Friend> friends = friendRepository.findFriendsByUserId(userId);
        return friends.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    protected void executeDeleteFriend(Long relationId, Long userId) {
        validateHelper.user(userId);
        validateHelper.friend(relationId);

        Friend friend = getFriend(relationId);
        friendRepository.delete(friend);
    }

    /*
    validator
     */

    //유저 유효성 검증

    @Override
    protected Boolean validateRelation(Long friendId, Long userId) {
        return Boolean.TRUE.equals(friendRepository.findFollowByFollowerIdAndFolloweeId(friendId, userId));
    }

    //친구관계가 true 인지 false 인지
    @Override
    protected Boolean validateRelation(Long relationId) {
        return friendRepository.findFollowById(relationId);
    }

    //이미 요청 중인 관계인지
    @Override
    protected void validateFollowExists(Long friendId, Long userId) {
        if (friendRepository.existsByFollowerIdAndFolloweeId(friendId, userId)){
            throw new CustomException(ErrorCode.ALREADY_FRIEND_REQUEST);
        }
    }

    // 수락/거절 권한 확인(상대방이 나에게 팔로우를 건 것이 맞는지)
    @Override
    protected void validateAuthority(Long relationId, Long userId) {
        Friend friend = getFriend(relationId);

        if (!friend.getFollowee().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
    }

    @Override
    protected void validateDelete(Long relationId, Long userId) {
        Friend friend = getFriend(relationId);

        if (!friend.getFollowee().getId().equals(userId) && !friend.getFollower().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
    }

    /*
    helper method
     */

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Friend getFriend(Long friendId) {
        return friendRepository.findById(friendId)
                .orElseThrow(() -> new CustomException(ErrorCode.RELATION_NOT_FOUND));
    }

}
