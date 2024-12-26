package com.example.newsfeed.controller;

import com.example.newsfeed.dto.BaseResponseDto;
import com.example.newsfeed.dto.post.PostMessageResponseDto;
import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
import com.example.newsfeed.mapper.BaseResponseMapper;
import com.example.newsfeed.service.PostLikeService;
import com.example.newsfeed.service.PostService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private static final String PAGE_COUNT = "1";
    private static final String PAGE_SIZE = "10";

    private final PostService postService;
    private final PostLikeService postLikeService;

    // Create a post
    @PostMapping
    public ResponseEntity<BaseResponseDto<PostResponseDto>> createPost(
            HttpServletRequest req,
            @Valid @RequestBody PostRequestDto dto) {

        Long userId = getUserId(req);
        PostResponseDto postResponseDto = postService.createPost(dto, userId);

        return ResponseEntity.ok(BaseResponseMapper.map(postResponseDto));
    }

    // Get friends' posts
    @GetMapping
    public ResponseEntity<BaseResponseDto<List<PostResponseDto>>> getFriendPosts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Long userId = getUserId(request);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        List<PostResponseDto> posts = postService.getPosts(userId, sort);

        return ResponseEntity.ok(BaseResponseMapper.map(posts));
    }

    // Read a post
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponseDto<PostResponseDto>> getPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size) {

        Pageable pageable = validatePageSize(page, size);
        PostResponseDto postResponseDto = postService.readPost(postId, pageable);

        return ResponseEntity.ok(BaseResponseMapper.map(postResponseDto));
    }

    // Update a post
    @PatchMapping("/{postId}")
    public ResponseEntity<BaseResponseDto<PostResponseDto>> updatePost(
            HttpServletRequest req,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto dto) {

        Long userId = getUserId(req);
        PostResponseDto postResponseDto = postService.updatePost(postId, dto, userId);

        return ResponseEntity.ok(BaseResponseMapper.map(postResponseDto));
    }

    // Like a post
    @PostMapping("/{postId}/likes")
    public ResponseEntity<BaseResponseDto<PostMessageResponseDto>> likePost(
            HttpServletRequest req,
            @PathVariable Long postId) {

        Long userId = getUserId(req);
        postLikeService.likePost(postId, userId);

        PostMessageResponseDto messageResponseDto = new PostMessageResponseDto("좋아요가 성공적으로 추가되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Dislike a post
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<BaseResponseDto<PostMessageResponseDto>> dislikePost(
            HttpServletRequest req,
            @PathVariable Long postId) {

        Long userId = getUserId(req);
        postLikeService.dislikePost(postId, userId);

        PostMessageResponseDto messageResponseDto = new PostMessageResponseDto("좋아요가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Delete a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponseDto<PostMessageResponseDto>> deletePost(
            HttpServletRequest req,
            @PathVariable Long postId) {

        Long userId = getUserId(req);
        postService.deletePost(userId, postId);

        PostMessageResponseDto messageResponseDto = new PostMessageResponseDto("게시물이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(BaseResponseMapper.map(messageResponseDto));
    }

    // Page size validator with return Pageable object
    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    /*
    Helper method
     */
    private static Long getUserId(HttpServletRequest req) {
        return SessionUserUtils.getId(req);
    }
}
