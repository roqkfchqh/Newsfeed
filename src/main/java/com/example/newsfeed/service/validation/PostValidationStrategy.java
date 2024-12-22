package com.example.newsfeed.service.validation;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.service.validation.normal.SingleValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostValidationStrategy implements SingleValidationStrategy<Long> {

    private final PostRepository postRepository;

    @Override
    public void validate(Long postId){
        if(postId == null){
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
    }
}
