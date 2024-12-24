package com.example.newsfeed.service;

import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.model.Friend;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.FriendAbstractService;
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

    @Transactional
    @Override
    public FriendResponseDto executeCreateFriend(
            Long friendId,
            Long userId
    ){
        //friend create(요청) 하는 부분이니까 일단 userid 받아서 validate -> 상대방도 validate -> 이미 친구관계에 등록되어있는지 validate -> create friend -> save

        User followee = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (friendRepository.existsByFollowerAndFollowee(userId, followee)) {
            throw new RuntimeException("이미 팔로우 요청을 보냈습니다.");
        }

        Friend friend = Friend.create(userId, followee);

        Friend savedFriend = friendRepository.save(friend);
        return toResponseDto(savedFriend);
    }

    @Transactional
    public void acceptFriend(Long friendId, Long userId) {

        //"내가 수락" 하는 부분 : action 은 controller 에서 검증 할 것
        //update : user validate -> friend validate -> 요청이 존재하는지 확인 -> 요청 수락 / 거절 권한 확인 -> 요청 수락 -> return string
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));

        // 수락/거절 권한 확인
        if (!friend.getFollowee().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 요청 상태 업데이트
        friend.acceptFollow(); // 요청 수락
        friendRepository.save(friend);
        //end Point를 두개를 두기
    }

    public void rejectFriend(Long friendId, Long userId) {

        //"내가 수락" 하는 부분 : action 은 controller 에서 검증 할 것
        //update : user validate -> friend validate -> 요청이 존재하는지 확인 -> 요청 수락 / 거절 권한 확인 -> 요청 수락 -> return string
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));

        if (!friend.getFollowee().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        friendRepository.delete(friend);
    }

    //getFollowers -> 나에게 요청을 보낸 사람이라면, 친구요청 수락받는 창
    //repository 에서 followeeId = userId고, 친구관계가 존재하는데 false 인 사람만
    //user validate -> 위의 내용 가져오기.
    public List<FriendResponseDto> getFollowers(Long userId){

        List<Friend> followers = friendRepository.findByFollower(currentUser);
        return null;
    }

    //내가 친구 요청을 보낸 사람들
    //repository 에서 followerId = userId고, 친구관계가 존재하는데 false 인 사람만
    //user validate -> 위의 내용 가져오기.
    public List<FriendResponseDto> getFollowees(Long userId) {
        List<Friend> followees = friendRepository.findByFollowee(currentUser);
        return followees.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    //친구들 목록
    //친구 관계가 존재하는데 true인 사람만 가져오는 기능.
    //user validate -> 위의 내용 가져오기.
    public List<FriendResponseDto> getFriends(Long userId) {

        return null;
    }


    //user validate -> friend validate -> 원래 친구였는지 확인 -> 권한 있는지 확인 -> 삭제로직
    @Transactional
    public void deleteFriend(Long friendId, Long userId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 관계입니다."));

        if (!friend.getFollowee().equals(currentUser)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        friendRepository.delete(friend);
    }

    /*
    validator
     */

    /*
    helper method
     */

}
