package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;

public abstract class PostAbstractService {

    //create
    public final PostResponseDto createPost(PostRequestDto dto, Long userId) {
        userValidator(userId);
        return executeCreatePost(dto, userId);
    }

    //read
    public final PostResponseDto readPost(Long postId){
        userValidator(postId);
        return executeReadPost(postId);
    }

    //update
    public final PostResponseDto updatePost(Long postId, PostRequestDto dto, Long userId) {
        userValidator(userId);
        postValidator(postId);
        return executeUpdatePost(postId, dto, userId);
    }

    //delete
    public final void deletePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        executeDeletePost(postId);
    }

    //like
    public final void likePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        likeValidator(postId, userId);
        executeLikePost(postId, userId);
    }

    //dislike
    public final void dislikePost(Long postId, Long userId) {
        userValidator(userId);
        postValidator(postId);
        dislikeValidator(postId, userId);
        executeDislikePost(postId, userId);
    }

    //validator
    protected abstract void userValidator(Long userId);
    protected abstract void postValidator(Long postId);
    protected abstract void likeValidator(Long postId, Long userId);
    protected abstract void dislikeValidator(Long postId, Long userId);

    //business logic
    protected abstract PostResponseDto executeCreatePost(PostRequestDto dto, Long userId);
    protected abstract PostResponseDto executeReadPost(Long postId);
    protected abstract PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId);
    protected abstract void executeDeletePost(Long postId);
    protected abstract void executeLikePost(Long postId, Long userId);
    protected abstract void executeDislikePost(Long postId, Long userId);


}
