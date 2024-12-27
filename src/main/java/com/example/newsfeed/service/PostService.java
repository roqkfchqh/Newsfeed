package com.example.newsfeed.service;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.PostMapper;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.template.PostAbstractService;
import com.example.newsfeed.service.validate.ValidateHelper;
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
    private final CommentRepository commentRepository;
    private final ValidateHelper validateHelper;

    @Override
    @Transactional
    protected PostResponseDto executeCreatePost(PostRequestDto dto, Long userId) {
        validateHelper.user(userId);

        User user = getUser(userId);
        Post post = PostMapper.toEntity(dto.getTitle(), dto.getContent(), user);

        postRepository.save(post);
        return PostMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    protected List<PostResponseDto> executeGetPosts(Long userId, Sort sort) {
        validateHelper.user(userId);

        List<Post> posts = postRepository.findPostsByFriends(userId, sort);
        return posts.stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    protected PostResponseDto executeReadPost(Long postId, Pageable pageable) {
        validateHelper.post(postId);

        Post post = getPost(postId);
        List<CommentResponseDto> comments = commentRepository.findCommentsByPostId(postId, pageable).getContent();

        return PostMapper.toDto(post, comments);
    }

    @Override
    @Transactional
    protected PostResponseDto executeUpdatePost(Long postId, PostRequestDto dto, Long userId) {
        validateHelper.user(userId);
        validateHelper.post(postId);

        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());

        return PostMapper.toDto(post);
    }

    @Override
    @Transactional
    protected void executeDeletePost(Long postId, Long userId) {
        validateHelper.user(userId);
        validateHelper.post(postId);

        postRepository.deleteById(postId);
    }

    /*
    validator
    */

    @Override
    protected void validateAuthority(Long postId, Long userId) {
        Post post = getPost(postId);
        if (!Objects.equals(post.getUser().getId(), userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
    }

    /*
    helper method
     */

    private Post getPost(Long postId) {
        return postRepository.findPostWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
