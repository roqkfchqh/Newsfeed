package com.example.newsfeed.service;

import com.example.newsfeed.mapper.CommentMapper;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Comment;
import com.example.newsfeed.model.CommentLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentLikeRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import com.example.newsfeed.service.validate_template.CommentLikeAbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService extends CommentLikeAbstractService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    protected void executeLikeComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);

        User user = getUser(userId);

        CommentLike like = CommentMapper.toEntity(user, comment);

        commentLikeRepository.save(like);

        comment.increaseLikeCount();
    }

    @Override
    @Transactional
    protected void executeUnlikeComment(Long userId, Long commentId) {
        Comment comment = getComment(commentId);

        CommentLike like = getCommentLike(userId, commentId);

        commentLikeRepository.delete(like);

        comment.decreaseLikeCount();
    }

    @Override
    protected void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Override
    protected void validateComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    @Override
    protected void validateNotAlreadyLiked(Long userId, Long commentId) {
        if (commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }

    @Override
    protected void validateLikeExists(Long userId, Long commentId) {
        if (commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isEmpty()) {
            throw new CustomException(ErrorCode.LIKE_NOT_FOUND);
        }
    }

    /*
    helper method
     */

    private User getUser(Long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private CommentLike getCommentLike(Long userId, Long commentId) {
        return commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_NOT_FOUND));
    }
}
