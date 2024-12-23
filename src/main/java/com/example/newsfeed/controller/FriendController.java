package com.example.newsfeed.controller;

import com.example.newsfeed.dto.friend.FriendRequestDto;
import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.exception.BaseException;
import com.example.newsfeed.model.User;
import com.example.newsfeed.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
        User currentUser = getCurrentUser(request);
        FriendResponseDto req = friendService.createFriend(requestDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(req);
    }

    //session required
    //update PATCH /friends/{friendId}
    //response: status, follower, followee
    @PatchMapping("/{friendId}")
    public ResponseEntity<?> updateFriend(
            @PathVariable("friendId") long friendId,
            @RequestBody String action,
            @RequestBody HttpServletRequest request
    ) {
        User currentUser = getCurrentUser(request);
        FriendResponseDto req = friendService.updateFriendStatus(friendId, action, currentUser);

        if ("REJECT".equalsIgnoreCase(action)) {
            return ResponseEntity.ok("팔로우 요청이 거절되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(req);
    }

    //session required
    //read GET /friends/follower
    //response: List<follower>
    @GetMapping("/follower")
    public ResponseEntity<List<FriendResponseDto>> getFollowers(
            HttpServletRequest request
    ) {
        User currentUser = getCurrentUser(request);
        List<FriendResponseDto> followers = friendService.getFollowers(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(followers);
    }

    //session required
    //read GET /friends/followee
    //response: List<followee>
    @GetMapping("followee")
    public ResponseEntity<List<FriendResponseDto>> getFollowees(
            HttpServletRequest request
    ) {
        User currentUser = getCurrentUser(request);
        List<FriendResponseDto> followees = friendService.getFollowees(currentUser);
        return ResponseEntity.status(HttpStatus.OK).body(followees);
    }

    //session required
    //delete DELETE /friends/{friendId}
    //response: 삭제 성공 메세지
    @DeleteMapping("/{friendId}/unFollow")
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long friendId,
            HttpServletRequest request
    ) {
        User currentUser = getCurrentUser(request);
        friendService.deleteFriend(friendId, currentUser);
        return ResponseEntity.ok("언팔로우 성공");
    }

    private User getCurrentUser(HttpServletRequest request) {
        //세션에서 사용자 정보 조회
        HttpSession session = request.getSession(false);
        if (session != null || session.getAttribute("user") != null) {
            throw new BaseException("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED) {
                @Override
                public HttpStatus getStatus() {
                    return super.getStatus();
                }
            };
        }
        return (User) session.getAttribute("user");
    }
}
