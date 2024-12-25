package com.example.newsfeed.service.validate;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.CommentRepository;
import com.example.newsfeed.repository.FriendRepository;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateHelper {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FriendRepository friendRepository;

    public void user(Long userId) {
        if(userId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void post(Long postId) {
        if(postId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        if (!postRepository.existsById(postId)) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }
    }

    public void comment(Long commentId) {
        if(commentId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        if (!commentRepository.existsById(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void friend(Long relationId) {
        if(relationId == null) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        if (!friendRepository.existsById(relationId)) {
            throw new CustomException(ErrorCode.RELATION_NOT_FOUND);
        }
    }
}
