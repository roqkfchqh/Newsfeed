package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.comment.CommentMessageResponseDto;
import com.example.newsfeed.dto.comment.CommentRequestDto;
import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.mapper.BaseResponseMapper;
import com.example.newsfeed.service.CommentLikeService;
import com.example.newsfeed.service.CommentService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    // Create a new comment (POST /comments)
    @PostMapping
    public ResponseEntity<BaseResponseDto<CommentResponseDto>> createComment(
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        CommentResponseDto responseDto = commentService.createComment(userId, requestDto);
        return ResponseEntity.ok(BaseResponseMapper.map(responseDto));
    }

    // Update an existing comment (PATCH /comments/{commentId})
    @PatchMapping("/{commentId}")
    public ResponseEntity<BaseResponseDto<CommentResponseDto>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        CommentResponseDto responseDto = commentService.updateComment(userId, commentId, requestDto);
        return ResponseEntity.ok(BaseResponseMapper.map(responseDto));
    }

    // Read a specific comment (GET /comments/{commentId})
    @GetMapping("/{commentId}")
    public ResponseEntity<BaseResponseDto<CommentResponseDto>> getComment(
            @PathVariable Long commentId) {
        CommentResponseDto responseDto = commentService.getComment(commentId);
        return ResponseEntity.ok(BaseResponseMapper.map(responseDto));
    }

    // Get all comments (GET /comments)
    @GetMapping
    public ResponseEntity<BaseResponseDto<List<CommentResponseDto>>> getAllComments() {
        List<CommentResponseDto> responseDtos = commentService.getAllComments();
        return ResponseEntity.ok(BaseResponseMapper.map(responseDtos));
    }

    // Like a comment (POST /comments/{commentId}/likes)
    @PostMapping("/{commentId}/likes")
    public ResponseEntity<BaseResponseDto<CommentMessageResponseDto>> likeComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        commentLikeService.likeComment(userId, commentId);
        CommentMessageResponseDto messageResponseDto = new CommentMessageResponseDto("좋아요가 성공적으로 추가되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Unlike a comment (DELETE /comments/{commentId}/likes)
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<BaseResponseDto<CommentMessageResponseDto>> unlikeComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        commentLikeService.unlikeComment(userId, commentId);
        CommentMessageResponseDto messageResponseDto = new CommentMessageResponseDto("좋아요가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Delete a comment (DELETE /comments/{commentId})
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponseDto<CommentMessageResponseDto>> deleteComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        commentService.deleteComment(userId, commentId);
        CommentMessageResponseDto messageResponseDto = new CommentMessageResponseDto("댓글이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    /*
    Helper method
     */
    private static Long getUserId(HttpServletRequest request) {
        return SessionUserUtils.getId(request);
    }
}


