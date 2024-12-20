package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;

public abstract class PostAbstractService {

    public final PostResponseDto createPost(PostRequestDto dto, Long userId) {
        userValidator(userId);
        return executeCreatePost(dto, userId);
    }

    public final PostResponseDto updatePost(Long postId, PostRequestDto dto, Long userId) {
        userValidator(userId);
        postValidator(postId);
        return executeUpdatePost(postId, dto, userId);
    }

    public final void deletePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        executeDeletePost(postId);
    }

    public final void likePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        executeLikePost(postId, userId);
    }

    public final void dislikePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        executeDislikePost(postId);
    }


    protected abstract void userValidator(Long userId);

    protected abstract void postValidator(Long postId);

    protected abstract PostResponseDto executeCreatePost(PostRequestDto dto, Long userId);

    protected abstract PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId);

    protected abstract void executeDeletePost(Long postId);

    protected abstract void executeLikePost(Long postId, Long userId);

    protected abstract void executeDislikePost(Long postId);


}
