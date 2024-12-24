package com.example.newsfeed.controller;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorCode;
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

    //create post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            HttpServletRequest req,
            @Valid @RequestBody PostRequestDto dto){

        Long userId = getUserId(req);

        return ResponseEntity.ok(postService.createPost(dto, userId));
    }

    //get friends posts
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getFriendPosts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Long userId = getUserId(request);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        List<PostResponseDto> posts = postService.getPosts(userId, sort);

        return ResponseEntity.ok(posts);
    }

    //read
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = PAGE_COUNT) int page,
            @RequestParam(defaultValue = PAGE_SIZE) int size){

        Pageable pageable = validatePageSize(page, size);

        return ResponseEntity.ok(postService.readPost(postId, pageable));
    }

    //update
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            HttpServletRequest req,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto dto){

        Long userId = getUserId(req);

        return ResponseEntity.ok(postService.updatePost(postId, dto, userId));
    }

    //like
    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> likePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = getUserId(req);

        postService.likePost(postId, userId);

        return ResponseEntity.ok("좋아요가 추가되었습니다.");
    }

    //dislike
    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<String> dislikePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = getUserId(req);

        postService.dislikePost(postId, userId);

        return ResponseEntity.ok("좋아요가 삭제되었습니다.");
    }

    //softDelete
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = getUserId(req);

        postService.deletePost(userId, postId);

        return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
    }

    //page size validator with return Pageable object
    private Pageable validatePageSize(int page, int size) {
        if (page < 1 || size < 1) {
            throw new CustomException(ErrorCode.PAGING_ERROR);
        }
        return PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
    }

    /*
    helper method
     */
    private static Long getUserId(HttpServletRequest req) {
        return SessionUserUtils.getId(req);
    }
}
