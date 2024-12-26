package com.example.newsfeed.service;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.PostLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.PostLikeRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.template.PostLikeAbstractService;
import com.example.newsfeed.service.validate.ValidateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostLikeService extends PostLikeAbstractService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final ValidateHelper validateHelper;

    //likePost
    @Override
    @Transactional
    protected void executeLikePost(Long postId, Long userId) {
        validateHelper.user(userId);
        validateHelper.post(postId);

        Post post = getPost(postId);

        PostLike postLike = PostLike.of(post, getUser(userId));
        postLikeRepository.save(postLike);

        post.likeCnt();
    }

    //dislikePost
    @Override
    @Transactional
    protected void executeDislikePost(Long postId, Long userId) {
        validateHelper.user(userId);
        validateHelper.post(postId);

        PostLike postLike = getPostLike(postId, userId);

        postLikeRepository.delete(postLike);

        Post post = getPost(postId);
        post.dislikeCnt();
    }

    /*
    validator
     */

    @Override
    protected void validateOperation(Long postId, Long userId){
        Post post = getPost(postId);
        if(Objects.equals(post.getUser().getId(), userId)){
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION_LIKE);
        }
    }

    @Override
    protected void validateNotAlreadyLiked(Long postId, Long userId){
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    @Override
    protected void validateLikeExists(Long postId, Long userId){
        if(!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.NOT_LIKED);
        }
    }

    /*
    helper method(repository)
     */

    private Post getPost(Long postId) {
        return postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private User getUser(Long userId){
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private PostLike getPostLike(Long postId, Long userId) {
        return postLikeRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }
}
