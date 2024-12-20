package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.PostLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.PostLikeRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService extends PostAbstractService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    //createPost
    @Override
    protected PostResponseDto executeCreatePost(PostRequestDto dto, Long userId){
        User user = getUser(userId);
        Post post = Post.create(dto.getTitle(), dto.getContent(), user);

        postRepository.save(post);
        return PostResponseDto.toCreate(dto.getTitle(), dto.getContent(), getUsername(userId), post.getCreatedAt());
    }

    //updatePost
    @Override
    @Transactional
    protected PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId){
        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());

        LocalDateTime updatedAt = postRepository.findPostUpdatedAtById(postId);

        return PostResponseDto.toUpdate(dto.getTitle(), dto.getContent(), getUsername(userId), updatedAt);
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long postId){
        postRepository.deleteById(postId);
    }

    //getPost
    public PostResponseDto readPost(Long postId){
        return postRepository.findPostWithUsernameById(postId);
    }

    //likePost
    @Override
    @Transactional
    public void executeLikePost(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.NOT_LIKED);
        }

        post.likeCnt();
        postLikeRepository.save(PostLike.of(post, user));
    }

    //dislikePost
    @Override
    @Transactional
    public void executeDislikePost(Long postId) {
        Post post = getPost(postId);
        post.dislikeCnt();
        postLikeRepository.deleteById(postId);
    }

    @Override
    protected void userValidator(Long userId){
        if(userId == null){
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    protected void postValidator(Long postId){
        if(postId == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private String getUsername(Long userId) {
        return userRepository.findNameById(userId);
    }
}
