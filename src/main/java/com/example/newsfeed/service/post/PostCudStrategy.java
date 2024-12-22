package com.example.newsfeed.service.post;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.model.Post;
import com.example.newsfeed.model.User;
import com.example.newsfeed.repository.PostRepository;
import com.example.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostCudStrategy implements CudStrategy<PostResponseDto, PostRequestDto> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostResponseDto create(PostRequestDto dto, Long userId) {
        User user = getUser(userId);
        Post post = Post.create(dto.getTitle(), dto.getContent(), user);
        postRepository.save(post);
        return post.toDto();
    }

    @Override
    @Transactional
    public PostResponseDto update(PostRequestDto dto, Long userId, Long postId) {
        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());
        return post.toDto();
    }

    @Override
    public void delete(Long postId) {
        Post post = getPost(postId);
        postRepository.delete(post);
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
}
