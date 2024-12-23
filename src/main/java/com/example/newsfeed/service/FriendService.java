package com.example.newsfeed.service;

import com.example.newsfeed.dto.friend.FriendRequestDto;
import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.exception.BaseException;
import com.example.newsfeed.model.Friend;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendResponseDto createFriend(
            FriendRequestDto requestDto,
            User currentUser
    ) throws BaseException {
        User followee = userRepository.findById(requestDto.getFolloweeId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (friendRepository.existsByFollowerAndFollowee(currentUser, followee)) {
            throw new RuntimeException("이미 팔로우 요청을 보냈습니다.");
        }

        Friend friend = Friend.create(currentUser, followee);

        Friend savedFriend = friendRepository.save(friend);
        return toResponseDto(savedFriend);
    }

    @Transactional
    public FriendResponseDto updateFriendStatus(Long friendId, String action, Long userId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));

        // 수락/거절 권한 확인
        if (!friend.getFollowee().equals(currentUser)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 요청 상태 업데이트
        if ("ACCEPT".equalsIgnoreCase(action)) {
            friend.acceptFollow(); // 요청 수락
            return toResponseDto(friendRepository.save(friend));
        } else if ("REJECT".equalsIgnoreCase(action)) {
            friendRepository.delete(friend); // 요청 거절
            return null; // 거절 시 응답 데이터 없음
        } else {
            throw new RuntimeException("유효하지 않은 동작입니다.");
        }
        //end Point를 두개를 두기
    }

    //친구 관계를 팔로워랑 팔로잉 두개로 둘거냐
    //아니면 양방향으로 둘거냐

    //getfollowers -> 나에게 요청을 보낸 사람이라면, 친구요청 수락받는 창
    //repository에서 folloeweeId = userId고, 친구관계가 존재하는데 false 인 사람만
    public List<FriendResponseDto> getFollowers(User currentUser) {

        List<Friend> followers = friendRepository.findByFollower(currentUser);
        return followers.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    //내가 친구 요청을 보낸 사람들
    public List<FriendResponseDto> getFollowees(User currentUser) {
        List<Friend> followees = friendRepository.findByFollowee(currentUser);
        return followees.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    //친구들 목록
    //친구 관계가 존재하는데 true인 사람만 가져오는 기능.

    @Transactional
    public void deleteFriend(Long friendId, User currentUser) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 관계입니다."));

        if (!friend.getFollowee().equals(currentUser)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        friendRepository.delete(friend);
    }

    private FriendResponseDto toResponseDto(Friend friend) {
        return new FriendResponseDto(
                friend.getId(),
                friend.getFollower().getId(),
                friend.getFollowee().getId(),
                friend.getFollow()
        );
    }

    // 예외처리, patch 엔드포인트 두개 두기, 중복 코드 관리.
}
