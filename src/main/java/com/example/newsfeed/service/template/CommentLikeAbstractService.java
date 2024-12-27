package com.example.newsfeed.service.template;

public abstract class CommentLikeAbstractService {

    // Like a comment
    public final void likeComment(Long userId, Long commentId) {
        validateNotAlreadyLiked(userId, commentId);
        executeLikeComment(userId, commentId);
    }

    // Unlike a comment
    public final void unlikeComment(Long userId, Long commentId) {
        validateLikeExists(userId, commentId);
        executeUnlikeComment(userId, commentId);
    }


    //validator
    protected abstract void validateNotAlreadyLiked(Long userId, Long commentId);

    protected abstract void validateLikeExists(Long userId, Long commentId);

    // business logic
    protected abstract void executeLikeComment(Long userId, Long commentId);

    protected abstract void executeUnlikeComment(Long userId, Long commentId);
}