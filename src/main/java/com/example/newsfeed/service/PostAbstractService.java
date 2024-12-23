package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public abstract class PostAbstractService {

    //create
    public final PostResponseDto createPost(PostRequestDto dto, Long userId) {
        userValidatorForPost(userId);
        return executeCreatePost(dto, userId);
    }

    //get friends posts
    public final List<PostResponseDto> getPosts(Long userId, Sort sort) {
        userValidatorForPost(userId);
        return executeGetPosts(userId, sort);
    }

    //read
    public final PostResponseDto readPost(Long postId, Pageable pageable) {
        postValidatorForPost(postId);
        return executeReadPost(postId, pageable);
    }

    //update
    public final PostResponseDto updatePost(Long postId, PostRequestDto dto, Long userId) {
        authorityValidatorForPost(postId, userId);
        userValidatorForPost(userId);
        postValidatorForPost(postId);
        return executeUpdatePost(postId, dto, userId);
    }

    //softDelete
    public final void deletePost(Long postId, Long userId) {
        authorityValidatorForPost(postId, userId);
        userValidatorForPost(userId);
        postValidatorForPost(postId);
        executeDeletePost(postId);
    }

    //like
    public final void likePost(Long postId, Long userId) {
        userValidatorForPost(userId);
        postValidatorForPost(postId);
        operationValidatorForPost(postId, userId);
        likeValidatorForPost(postId, userId);
        executeLikePost(postId, userId);
    }

    //dislike
    public final void dislikePost(Long postId, Long userId) {
        userValidatorForPost(userId);
        postValidatorForPost(postId);
        operationValidatorForPost(postId, userId);
        dislikeValidatorForPost(postId, userId);
        executeDislikePost(postId, userId);
    }

    //validator
    protected abstract void userValidatorForPost(Long userId); //user 가 유효한지 검사
    protected abstract void postValidatorForPost(Long postId); //post 가 유효한지 검사

    protected abstract void authorityValidatorForPost(Long postId, Long userId);   //수정 등의 권한이 있는지 검사
    protected abstract void operationValidatorForPost(Long postId, Long userId);   //본인 게시글에 좋아요 가능 여부 검사

    protected abstract void likeValidatorForPost(Long postId, Long userId);    //like 이전, 이미 like 내역이 있는지 검사
    protected abstract void dislikeValidatorForPost(Long postId, Long userId); //dislike 이전, 이미 like 내역이 있는지 검사


    //business logic
    protected abstract PostResponseDto executeCreatePost(PostRequestDto dto, Long userId);
    protected abstract List<PostResponseDto> executeGetPosts(Long userId, Sort sort);
    protected abstract PostResponseDto executeReadPost(Long postId, Pageable pageable);
    protected abstract PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId);
    protected abstract void executeDeletePost(Long postId);
    protected abstract void executeLikePost(Long postId, Long userId);
    protected abstract void executeDislikePost(Long postId, Long userId);
}
