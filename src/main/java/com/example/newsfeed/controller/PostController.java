package com.example.newsfeed.controller;

import com.example.newsfeed.dto.post.PostRequestDto;
import com.example.newsfeed.dto.post.PostResponseDto;
import com.example.newsfeed.service.PostService;
import com.example.newsfeed.session.SessionUserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            HttpServletRequest req,
            @Valid @RequestBody PostRequestDto dto){

        Long userId = SessionUserUtils.getId(req);

        return ResponseEntity.ok(postService.createPost(dto, userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId){

        return ResponseEntity.ok(postService.readPost(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            HttpServletRequest req,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto dto){

        Long userId = SessionUserUtils.getId(req);

        return ResponseEntity.ok(postService.updatePost(postId, dto, userId));
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<String> likePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = SessionUserUtils.getId(req);

        postService.likePost(postId, userId);

        return ResponseEntity.ok("좋아요가 추가되었습니다.");
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<String> dislikePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = SessionUserUtils.getId(req);

        postService.dislikePost(postId, userId);

        return ResponseEntity.ok("좋아요가 삭제되었습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            HttpServletRequest req,
            @PathVariable Long postId){

        Long userId = SessionUserUtils.getId(req);

        postService.deletePost(userId, postId);

        return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
    }

    /*
    [Posts]
    //session required
    게시물 생성 POST /posts

    게시물 조회 GET /posts

    //session required
    게시물 갱신 PATCH /posts/{postId}

    //session required
    //좋아요 갱신 POST /posts/{postId}/likes
    //response: 성공 메세지

    //session required
    //좋아요 갱신 DELETE /posts/{postId}/likes
    //response: 삭제 성공 메세지

    //session required
    게시물 삭제 DELETE /posts/{postId}

     */
}
