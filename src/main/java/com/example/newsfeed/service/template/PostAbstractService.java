package com.example.newsfeed.service.template;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public abstract class PostAbstractService {

    // Create a new post
    public final PostResponseDto createPost(PostRequestDto dto, Long userId) {
        return executeCreatePost(dto, userId);
    }

    // Get posts from a user's friends
    public final List<PostResponseDto> getPosts(Long userId, Sort sort) {
        return executeGetPosts(userId, sort);
    }

    // Read a specific post by its ID
    public final PostResponseDto readPost(Long postId, Pageable pageable) {
        return executeReadPost(postId, pageable);
    }

    // Update an existing post
    public final PostResponseDto updatePost(Long postId, PostRequestDto dto, Long userId) {
        validateAuthority(postId, userId);
        return executeUpdatePost(postId, dto, userId);
    }

    // Delete an existing post
    public final void deletePost(Long postId, Long userId) {
        validateAuthority(postId, userId);
        executeDeletePost(postId, userId);
    }


    //validator
    protected abstract void validateAuthority(Long postId, Long userId);   //수정 등의 권한이 있는지 검사

    //business logic
    protected abstract PostResponseDto executeCreatePost(PostRequestDto dto, Long userId);

    protected abstract List<PostResponseDto> executeGetPosts(Long userId, Sort sort);

    protected abstract PostResponseDto executeReadPost(Long postId, Pageable pageable);

    protected abstract PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId);

    protected abstract void executeDeletePost(Long postId, Long userId);
}

