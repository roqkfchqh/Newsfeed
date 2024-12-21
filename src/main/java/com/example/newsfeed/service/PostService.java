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
        return post.toDto(getUsername(userId));
    }

    //updatePost
    @Override
    @Transactional
    protected PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId){
        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());

        return post.toDto(getUsername(userId));
    }

    //deletePost
    @Override
    protected void executeDeletePost(Long postId){
        postRepository.deleteById(postId);
    }

    //getPost
    @Override
    public PostResponseDto executeReadPost(Long postId){
        return postRepository.findPostWithUsernameById(postId);
    }

    //likePost
    @Override
    @Transactional
    public void executeLikePost(Long postId, Long userId) {
        Post post = getPost(postId);

        PostLike postLike = PostLike.of(post, getUser(userId));
        postLikeRepository.save(postLike);

        post.likeCnt();
    }

    //dislikePost
    @Override
    @Transactional
    public void executeDislikePost(Long postId, Long userId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        postLikeRepository.delete(postLike);

        Post post = getPost(postId);
        post.dislikeCnt();
    }

    /*
    validator
    */

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

    @Override
    protected void likeValidator(Long postId, Long userId){
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    @Override
    protected void dislikeValidator(Long postId, Long userId){
        if(!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.NOT_LIKED);
        }
    }

    /*
    getter method(repository)
     */

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
