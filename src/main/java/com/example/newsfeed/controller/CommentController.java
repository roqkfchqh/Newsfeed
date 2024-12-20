package com.example.newsfeed.controller;

import com.example.newsfeed.dto.CommentRequestDto;
import com.example.newsfeed.dto.CommentResponseDto;
import com.example.newsfeed.service.CommentService;
import com.example.newsfeed.util.SessionUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // Create a new comment (POST /comments)
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        CommentResponseDto responseDto = commentService.createComment(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Update an existing comment (PATCH /comments/{commentId})
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        CommentResponseDto responseDto = commentService.updateComment(userId, commentId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Read a specific comment (GET /comments/{commentId})
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        CommentResponseDto responseDto = commentService.getComment(commentId);
        return ResponseEntity.ok(responseDto);
    }

    // Get all comments (GET /comments)
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComments(@RequestParam(value = "postId", required = false) Long postId) {
        List<CommentResponseDto> responseDtos = commentService.getAllComments(postId);
        return ResponseEntity.ok(responseDtos);
    }

    // Like a comment (POST /comments/{commentId}/likes)
    @PostMapping("/{commentId}/likes")
    public ResponseEntity<String> likeComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        commentService.likeComment(userId, commentId);
        return ResponseEntity.ok("Like added successfully");
    }

    // Unlike a comment (DELETE /comments/{commentId}/likes)
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<String> unlikeComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        commentService.unlikeComment(userId, commentId);
        return ResponseEntity.ok("Like removed successfully");
    }

    // Delete a comment (DELETE /comments/{commentId})
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            HttpServletRequest request) {
        Long userId = SessionUserUtils.getId(request);
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
