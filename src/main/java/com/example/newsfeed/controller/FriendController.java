package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.friend.FriendMessageResponseDto;
import com.example.newsfeed.dto.friend.FriendResponseDto;
import com.example.newsfeed.mapper.BaseResponseMapper;
import com.example.newsfeed.service.FriendService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {

    private final FriendService friendService;

    // Create a new friend request (POST /friends/{friendId})
    @PostMapping("/{friendId}")
    public ResponseEntity<BaseResponseDto<FriendResponseDto>> createFriend(
            @PathVariable("friendId") Long friendId,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        FriendResponseDto responseDto = friendService.createFriend(friendId, userId);
        return ResponseEntity.ok(BaseResponseMapper.map(responseDto));
    }

    // Accept a friend request (PATCH /friends/{relationId}/accept)
    @PatchMapping("/{relationId}/accept")
    public ResponseEntity<BaseResponseDto<FriendMessageResponseDto>> acceptFriend(
            @PathVariable("relationId") Long relationId,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        friendService.acceptFriend(relationId, userId);
        FriendMessageResponseDto messageResponseDto = new FriendMessageResponseDto("친구 요청을 수락했습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Reject a friend request (DELETE /friends/{relationId}/reject)
    @DeleteMapping("/{relationId}/reject")
    public ResponseEntity<BaseResponseDto<FriendMessageResponseDto>> rejectFriend(
            @PathVariable("relationId") Long relationId,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        friendService.rejectFriend(relationId, userId);
        FriendMessageResponseDto messageResponseDto = new FriendMessageResponseDto("친구 요청을 거절했습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Get list of friends (GET /friends/connection)
    @GetMapping("/connection")
    public ResponseEntity<BaseResponseDto<List<FriendResponseDto>>> getFriends(
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        List<FriendResponseDto> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(BaseResponseMapper.map(friends));
    }

    // Get list of followers who follow the user (GET /friends/follower)
    @GetMapping("/follower")
    public ResponseEntity<BaseResponseDto<List<FriendResponseDto>>> getFollowers(
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        List<FriendResponseDto> followers = friendService.getFollowers(userId);
        return ResponseEntity.ok(BaseResponseMapper.map(followers));
    }

    // Get list of followees who the user follows (GET /friends/followee)
    @GetMapping("/followee")
    public ResponseEntity<BaseResponseDto<List<FriendResponseDto>>> getFollowees(
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        List<FriendResponseDto> followees = friendService.getFollowees(userId);
        return ResponseEntity.ok(BaseResponseMapper.map(followees));
    }

    // Delete a friend relationship (DELETE /friends/{relationId})
    @DeleteMapping("/{relationId}")
    public ResponseEntity<BaseResponseDto<FriendMessageResponseDto>> deleteFriend(
            @PathVariable Long relationId,
            HttpServletRequest request
    ) {
        Long userId = getUserId(request);
        friendService.deleteFriend(relationId, userId);
        FriendMessageResponseDto messageResponseDto = new FriendMessageResponseDto("친구 관계가 삭제되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    /*
    Helper method
     */
    private static Long getUserId(HttpServletRequest request) {
        return SessionUserUtils.getId(request);
    }
}
