package com.example.newsfeed.service.validation;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.PostLikeRepository;
import com.example.newsfeed.service.validation.normal.DualValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeValidationStrategy implements DualValidationStrategy<Long> {

    private final PostLikeRepository postLikeRepository;

    @Override
    public void validate(Long postId, Long userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }
    }
}
