package com.example.newsfeed.controller;

import com.example.newsfeed.dto.friend.FriendRequestDto;
import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.service.FriendService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    //follower < 나를 팔로우 하는 사람들(팔로워)
    //followee < 내가 팔로우 하는 사람들(구독)

    private final FriendService friendService;

    //session required
    //create POST /friends
    //request: followee
    //response: status, followee
    @PostMapping
    public ResponseEntity<FriendResponseDto> createFriend(
            @RequestBody @Valid FriendRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        FriendResponseDto req = friendService.createFriend(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(req);
    }

    //session required
    //update PATCH /friends/{friendId}
    //response: status, follower, followee
    //friendId/accept , friendId/reject
    //친구 승낙
    @PatchMapping("/{friendId}/accept")
    public ResponseEntity<?> acceptFriend(
            @PathVariable("friendId") long friendId,
            @RequestBody String action,
            @RequestBody HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        FriendResponseDto req = friendService.updateFriendStatus(friendId, action, userId);

        if ("REJECT".equalsIgnoreCase(action)) {
            return ResponseEntity.ok("팔로우 요청이 거절되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(req);
    }

    //친구 거절
    @DeleteMapping("/{friendId}/reject")
    public ResponseEntity<?> rejectFriend(

    ){

        return null;
    }

    //친구 목록 불러오기
    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> getFriends(

    ){

        return null;
    }

    //session required
    //read GET /friends/follower
    //response: List<follower>
    //친구요청 보낸 목록 불러오기
    @GetMapping("/follower")
    public ResponseEntity<List<FriendResponseDto>> getFollowers(
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        List<FriendResponseDto> followers = friendService.getFollowers(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    //친구요청 받은 목록 불러오기
    //session required
    //read GET /friends/followee
    //response: List<followee>
    @GetMapping("/followee")
    public ResponseEntity<List<FriendResponseDto>> getFollowees(
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        List<FriendResponseDto> followees = friendService.getFollowees(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followees);
    }

    //session required
    //softDelete DELETE /friends/{friendId}
    //response: 삭제 성공 메세지
    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long friendId,
            HttpServletRequest request
    ) {
        Long userId = SessionUserUtils.getId(request);
        friendService.deleteFriend(friendId, userId);
        return ResponseEntity.ok("언팔로우 성공");
    }
}
