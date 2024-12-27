package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.comment.CommentRequestDto;
import com.example.newsfeed.dto.comment.CommentResponseDto;

import java.util.List;

public abstract class CommentAbstractService {

    // Create a new comment
    public final CommentResponseDto createComment(Long userId, CommentRequestDto requestDto) {
        return executeCreateComment(userId, requestDto);
    }

    // Update an existing comment
    public final CommentResponseDto updateComment(Long userId, Long commentId, CommentRequestDto requestDto) {
        validateUserOwnership(userId, commentId);
        return executeUpdateComment(userId, commentId, requestDto);
    }

    // Get a specific comment by its ID
    public final CommentResponseDto getComment(Long commentId) {
        return executeGetComment(commentId);
    }

    // Get all comments
    public final List<CommentResponseDto> getAllComments() {
        return executeGetAllComments();
    }

    // Delete a comment
    public final void deleteComment(Long userId, Long commentId) {
        validateUserOwnership(userId, commentId);
        executeDeleteComment(commentId);
    }


    //validator
    protected abstract void validateUserOwnership(Long userId, Long commentId);

    //business logic
    protected abstract CommentResponseDto executeCreateComment(Long userId, CommentRequestDto requestDto);

    protected abstract CommentResponseDto executeUpdateComment(Long userId, Long commentId, CommentRequestDto requestDto);

    protected abstract CommentResponseDto executeGetComment(Long commentId);

    protected abstract List<CommentResponseDto> executeGetAllComments();

    protected abstract void executeDeleteComment(Long commentId);


}

