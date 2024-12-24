package com.example.newsfeed.service.validate_template;

public abstract class CommentLikeAbstractService {

    public final void likeComment(Long userId, Long commentId) {
        validateUser(userId);
        validateComment(commentId);
        validateNotAlreadyLiked(userId, commentId);
        executeLikeComment(userId, commentId);
    }

    public final void unlikeComment(Long userId, Long commentId) {
        validateUser(userId);
        validateComment(commentId);
        validateLikeExists(userId, commentId);
        executeUnlikeComment(userId, commentId);
    }

    // Validation methods
    protected abstract void validateUser(Long userId);
    protected abstract void validateComment(Long commentId);
    protected abstract void validateNotAlreadyLiked(Long userId, Long commentId);
    protected abstract void validateLikeExists(Long userId, Long commentId);

    // Execution methods
    protected abstract void executeLikeComment(Long userId, Long commentId);
    protected abstract void executeUnlikeComment(Long userId, Long commentId);
}
