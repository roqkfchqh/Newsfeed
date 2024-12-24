package com.example.newsfeed.service;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.PostMapper;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.PostLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.PostLikeRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.PostAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService extends PostAbstractService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    //create
    @Override
    protected PostResponseDto executeCreatePost(PostRequestDto dto, Long userId){
        User user = getUser(userId);
        Post post = PostMapper.toEntity(dto.getTitle(), dto.getContent(), user);

        postRepository.save(post);
        return PostMapper.toDto(post);
    }

    //get friends posts
    @Override
    protected List<PostResponseDto> executeGetPosts(Long userId, Sort sort){
        List<Post> posts = postRepository.findPostsByFriends(userId, sort);
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }

    //read
    @Override
    protected PostResponseDto executeReadPost(Long postId, Pageable pageable){
        Post post = getPost(postId);
        List<CommentResponseDto> comments = commentRepository.findCommentsByPostId(postId, pageable).getContent();

        return PostMapper.toDto(post, comments);
    }

    //update
    @Override
    @Transactional
    protected PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId){
        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());

        return PostMapper.toDto(post);
    }

    //delete
    @Override
    protected void executeDeletePost(Long postId){
        postRepository.deleteById(postId);
    }

    //likePost
    @Override
    @Transactional
    protected void executeLikePost(Long postId, Long userId) {
        Post post = getPost(postId);

        PostLike postLike = PostLike.of(post, getUser(userId));
        postLikeRepository.save(postLike);

        post.likeCnt();
    }

    //dislikePost
    @Override
    @Transactional
    protected void executeDislikePost(Long postId, Long userId) {
        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId);

        postLikeRepository.delete(postLike);

        Post post = getPost(postId);
        post.dislikeCnt();
    }

    /*
    validator
    */

    @Override
    protected void validateUser(Long userId){
        if(userId == null){
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    protected void validatePost(Long postId){
        if(postId == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    @Override
    protected void validateAuthority(Long postId, Long userId){
        Post post = getPost(postId);
        if(!Objects.equals(post.getId(), userId)){
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
    }

    @Override
    protected void validateOperation(Long postId, Long userId){
        Post post = getPost(postId);
        if(Objects.equals(post.getId(), userId)){
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
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private User getUser(Long userId){
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
