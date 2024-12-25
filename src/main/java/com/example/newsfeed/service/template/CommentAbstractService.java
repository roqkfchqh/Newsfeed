package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.comment.CommentRequestDto;
import com.example.newsfeed.dto.comment.CommentResponseDto;

import java.util.List;

public abstract class CommentAbstractService {

    public final CommentResponseDto createComment(Long userId, CommentRequestDto requestDto) {
        return executeCreateComment(userId, requestDto);
    }

    public final CommentResponseDto updateComment(Long userId, Long commentId, CommentRequestDto requestDto) {
        validateUserOwnership(userId, commentId);
        return executeUpdateComment(userId, commentId, requestDto);
    }

    public final CommentResponseDto getComment(Long commentId) {
        return executeGetComment(commentId);
    }

    public final List<CommentResponseDto> getAllComments() {
        return executeGetAllComments();
    }

    public final void deleteComment(Long userId, Long commentId) {
        validateUserOwnership(userId, commentId);
        executeDeleteComment(commentId);
    }

    // Validation methods
    protected abstract void validateUserOwnership(Long userId, Long commentId);

    // Execution methods
    protected abstract CommentResponseDto executeCreateComment(Long userId, CommentRequestDto requestDto);
    protected abstract CommentResponseDto executeUpdateComment(Long userId, Long commentId, CommentRequestDto requestDto);
    protected abstract CommentResponseDto executeGetComment(Long commentId);
    protected abstract List<CommentResponseDto> executeGetAllComments();
    protected abstract void executeDeleteComment(Long commentId);


}

