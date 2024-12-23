package com.example.newsfeed.service;

import com.example.newsfeed.dto.comment.CommentResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Comment;
import com.example.newsfeed.model.CommentLike;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.CommentLikeRepository;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponseDto likeComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (commentLikeRepository.findByUserIdAndCommentId(userId, commentId).isPresent()) {
            throw new IllegalArgumentException("Already liked this comment");
        }

        CommentLike like = CommentLike.builder()
                .user(user)
                .comment(comment)
                .build();

        commentLikeRepository.save(like);

        // 갱신된 댓글 정보 반환
        Comment updatedComment = commentRepository.findById(commentId).get();
        return CommentResponseDto.of(updatedComment);
    }

    public CommentResponseDto unlikeComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        CommentLike like = commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));

        commentLikeRepository.delete(like);

        // 갱신된 댓글 정보 반환
        Comment updatedComment = commentRepository.findById(commentId).get();
        return CommentResponseDto.of(updatedComment);
    }
}
