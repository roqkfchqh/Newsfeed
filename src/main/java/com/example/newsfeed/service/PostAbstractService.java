package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.dto.post.ReadPageResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public abstract class PostAbstractService {

    //create
    public final PostResponseDto createPost(PostRequestDto dto, Long userId) {
        userValidator(userId);
        return executeCreatePost(dto, userId);
    }

    //get friends posts
    public final List<PostResponseDto> getPosts(Long userId, Sort sort) {
        userValidator(userId);
        return executeGetPosts(userId, sort);
    }

    //read
    public final Map<PostResponseDto, List<ReadPageResponseDto>> readPost(Long postId, Pageable pageable) {
        userValidator(postId);
        return executeReadPost(postId, pageable);
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
    protected abstract List<PostResponseDto> executeGetPosts(Long userId, Sort sort);
    protected abstract Map<PostResponseDto, List<ReadPageResponseDto>> executeReadPost(Long postId, Pageable pageable);
    protected abstract PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId);
    protected abstract void executeDeletePost(Long postId);
    protected abstract void executeLikePost(Long postId, Long userId);
    protected abstract void executeDislikePost(Long postId, Long userId);
}
