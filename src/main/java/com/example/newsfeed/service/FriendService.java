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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService extends FriendAbstractService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FriendResponseDto executeCreateFriend(Long friendId, Long userId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        Friend relate = FriendMapper.toEntity(user, friend);

        Friend savedFriend = friendRepository.save(relate);
        return FriendMapper.toDto(savedFriend);
    }


    @Override
    @Transactional
    public void executeAcceptFriend(Long relationId, Long userId) {
        Friend friend = getFriend(relationId);
        friend.acceptFollow();
        friendRepository.save(friend);
    }

    @Override
    @Transactional
    public void executeRejectFriend(Long relationId, Long userId) {
        Friend friend = getFriend(relationId);
        friendRepository.delete(friend);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDto> executeGetFollowers(Long userId) {
        List<Friend> followers = friendRepository.findByFollower(userId);
        followers.forEach(friend -> log.info("Friend Data: {}", friend));
        return followers.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDto> executeGetFollowees(Long userId) {
        List<Friend> followees = friendRepository.findByFollowee(userId); //
        followees.forEach(friend -> log.info("Friend Data: {}", friend));
        return followees.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendResponseDto> executeGetFriends(Long userId) {
        List<Friend> friends = friendRepository.findFriendsByUserId(userId);
        if (friends.isEmpty()) {
            log.warn("No friends found for userId={} with query condition", userId);
        } else {
            friends.forEach(friend -> log.info("Friend found: {}", friend));
        }
        friends.stream().forEach(friend -> {
            log.info("Friend Id: {}, Follower: {}, Followee: {}, Follow: {}",
                    friend.getId(),
                    friend.getFollower().getId(),
                    friend.getFollowee().getId(),
                    friend.getFollow()
            );
        });
        return friends.stream()
                .map(FriendMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void executeDeleteFriend(Long relationId, Long userId) {
        Friend friend = getFriend(relationId);
        friendRepository.delete(friend);
    }


    /*
    validator
     */

    @Override
    protected Boolean validateRelation(Long friendId, Long userId) {
        return Boolean.TRUE.equals(friendRepository.findFollowByFollowerIdAndFolloweeId(friendId, userId));
    }

    @Override
    protected Boolean validateRelation(Long relationId) {
        return friendRepository.findFollowById(relationId);
    }

    @Override
    protected void validateFollowExists(Long friendId, Long userId) {
        if (friendRepository.existsByFollowerIdAndFolloweeId(friendId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_FRIEND_REQUEST);
        }
    }

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

    @Override
    protected void validateSelfRequest(Long friendId, Long userId) {
        if (friendId.equals(userId)) {
            throw new CustomException(ErrorCode.SELF_REQUEST_NOT_ALLOWED);
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
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

}
