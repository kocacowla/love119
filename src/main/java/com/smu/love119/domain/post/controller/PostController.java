package com.smu.love119.domain.post.controller;

import com.smu.love119.domain.post.dto.PostDTO;
import com.smu.love119.domain.post.dto.PostResponseDTO;
import com.smu.love119.domain.post.service.PostService;
import com.smu.love119.global.apiRes.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<List<PostResponseDTO>> getAllPosts() {
        return ApiResponse.successRes(HttpStatus.OK, postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO> getPostById(@PathVariable Long postId) {
        return ApiResponse.successRes(HttpStatus.OK, postService.getPostById(postId));
    }

    @PostMapping
    public ApiResponse<PostResponseDTO> registerPost(
            @Valid @RequestBody PostDTO postDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponse.successRes(HttpStatus.CREATED, postService.createPost(userDetails.getUsername(), postDTO));
    }
    @PutMapping("/{postId}")
    public ApiResponse<PostResponseDTO> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostDTO postDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        return ApiResponse.successRes(HttpStatus.OK, postService.updatePost(postId, postDTO, userDetails.getUsername()));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        postService.deletePost(postId, userDetails.getUsername());
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }

}
