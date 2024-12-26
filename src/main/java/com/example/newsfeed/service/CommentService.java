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
import com.example.newsfeed.service.template.CommentAbstractService;
import com.example.newsfeed.service.validate.ValidateHelper;
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
    private final ValidateHelper validateHelper;

    @Override
    @Transactional
    protected CommentResponseDto executeCreateComment(Long userId, CommentRequestDto requestDto) {
        validateHelper.user(userId);
        validateHelper.post(requestDto.getPostId());

        User user = getUser(userId);
        Post post = getPost(requestDto);

        Comment comment = CommentMapper.toEntity(user, post, requestDto.getContent());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    protected CommentResponseDto executeUpdateComment(Long userId, Long commentId, CommentRequestDto requestDto) {
        validateHelper.user(userId);
        validateHelper.comment(commentId);

        Comment comment = getCommentById(commentId);
        comment.updateContent(requestDto.getContent());
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    protected CommentResponseDto executeGetComment(Long commentId) {
        validateHelper.comment(commentId);

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
        validateHelper.comment(commentId);
        commentRepository.deleteById(commentId);
    }

    /*
    validator
     */

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