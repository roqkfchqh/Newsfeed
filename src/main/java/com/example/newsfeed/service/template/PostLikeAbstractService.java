package com.example.newsfeed.service.template;

public abstract class PostLikeAbstractService {

    // Like a post
    public final void likePost(Long postId, Long userId) {
        validateOperation(postId, userId);
        validateNotAlreadyLiked(postId, userId);
        executeLikePost(postId, userId);
    }

    // Dislike a post
    public final void dislikePost(Long postId, Long userId) {
        validateOperation(postId, userId);
        validateLikeExists(postId, userId);
        executeDislikePost(postId, userId);
    }


    //validator
    protected abstract void validateOperation(Long postId, Long userId);   //본인 게시글에 좋아요 가능 여부 검사

    protected abstract void validateNotAlreadyLiked(Long postId, Long userId);    //like 이전, 이미 like 내역이 있는지 검사

    protected abstract void validateLikeExists(Long postId, Long userId); //dislike 이전, 이미 like 내역이 있는지 검사

    //business logic
    protected abstract void executeLikePost(Long postId, Long userId);

    protected abstract void executeDislikePost(Long postId, Long userId);
}
