package com.example.newsfeed.service;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.dto.post.ReadPageResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.PostLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.PostLikeRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validation.normal.DualValidationStrategy;
import com.example.newsfeed.service.validation.normal.SingleValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService extends BaseAbstractService<PostResponseDto, PostRequestDto, ReadPageResponseDto> {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final SingleValidationStrategy<Long> userValidationStrategy;
    private final SingleValidationStrategy<Long> postValidationStrategy;
    private final DualValidationStrategy<Long> likeValidationStrategy;
    private final DualValidationStrategy<Long> dislikeValidationStrategy;

    //create
    @Override
    protected PostResponseDto executeCreate(PostRequestDto dto, Long userId){
        User user = getUser(userId);
        Post post = Post.create(dto.getTitle(), dto.getContent(), user);

        postRepository.save(post);
        return post.toDto(getUsername(userId));
    }

    //get friends posts
    @Override
    protected List<PostResponseDto> executeGetAll(Long userId, Sort sort){
        return postRepository.findPostsByFriends(userId, sort);
    }

    //read
    @Override
    public Map<PostResponseDto, List<ReadPageResponseDto>> executeGet(Long postId, Pageable pageable){
        Post post = getPost(postId);
        Page<ReadPageResponseDto> comments = commentRepository.findCommentsByPostId(postId, pageable);

        PostResponseDto result = post.toDto(getUsername(post.getUser().getId()));
        return Map.of(result, comments.getContent());
    }

    //update
    @Override
    @Transactional
    protected PostResponseDto executeUpdate(PostRequestDto dto, Long userId, Long postId){

        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());

        return post.toDto(getUsername(userId));
    }

    //delete
    @Override
    protected void executeDelete(Long postId){
        postRepository.deleteById(postId);
    }

    //likePost
    @Override
    @Transactional
    public void executeLike(Long postId, Long userId) {
        likeValidationStrategy.validate(postId, userId);

        Post post = getPost(postId);
        PostLike postLike = PostLike.of(post, getUser(userId));
        postLikeRepository.save(postLike);

        post.likeCnt();
    }

    //dislikePost
    @Override
    @Transactional
    public void executeDislike(Long postId, Long userId) {
        dislikeValidationStrategy.validate(postId, userId);

        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        postLikeRepository.delete(postLike);

        Post post = getPost(postId);
        post.dislikeCnt();
    }

    /*
    validator
    */

    @Override
    protected void userValidate(Long userId){
        userValidationStrategy.validate(userId);
    }

    @Override
    protected void validate(Long postId){
        postValidationStrategy.validate(postId);
    }

    /*
    helper method
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
