package com.example.newsfeed.service;

import com.example.newsfeed.dto.comment.CommentRequestDto;
import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.CommentMapper;
import com.example.newsfeed.model.Comment;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.CommentAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService extends CommentAbstractService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    protected CommentResponseDto executeCreateComment(Long userId, CommentRequestDto requestDto) {
        User user = getUser(userId);
        Post post = getPost(requestDto);

        Comment comment = CommentMapper.toEntity(user, post, requestDto.getContent());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    protected CommentResponseDto executeUpdateComment(Long userId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = getCommentById(commentId);
        comment.updateContent(requestDto.getContent());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    protected CommentResponseDto executeGetComment(Long commentId) {
        return CommentMapper.toDto(commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    protected List<CommentResponseDto> executeGetAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    protected void executeDeleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    /*
    validator
     */

    @Override
    protected void validateUser(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    protected void validatePost(Long postId) {
        if (postId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
    }

    @Override
    protected void validateComment(Long commentId) {
        if (commentId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        if (!commentRepository.existsById(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    @Override
    protected void validateUserOwnership(Long userId, Long commentId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OPERATION);
        }
    }

    /*
    helper method
     */

    private User getUser(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Post getPost(CommentRequestDto requestDto) {
        return postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}