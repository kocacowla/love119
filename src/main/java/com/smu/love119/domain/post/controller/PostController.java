package com.smu.love119.domain.post.controller;

import com.smu.love119.domain.post.dto.PostDTO;
import com.smu.love119.domain.post.dto.PostResponseDTO;
import com.smu.love119.domain.post.service.PostService;
import com.smu.love119.global.apiRes.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/latest")
    public ApiResponse<List<PostResponseDTO>> getLatestPosts(
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.successRes(HttpStatus.OK, postService.getLatestPosts(page));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/popular")
    public ApiResponse<List<PostResponseDTO>> getPopularPosts(
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.successRes(HttpStatus.OK, postService.getPopularPosts(page));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/search")
    public ApiResponse<List<PostResponseDTO>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page) {
        return ApiResponse.successRes(HttpStatus.OK, postService.searchPosts(keyword, page));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO> getPostById(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ApiResponse.successRes(
                HttpStatus.OK, postService.getPostById(postId, userDetails.getUsername())
        );
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ApiResponse<PostResponseDTO> registerPost(
            @Valid @RequestBody PostDTO postDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ApiResponse.successRes(HttpStatus.CREATED, postService.createPost(userDetails.getUsername(), postDTO));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{postId}")
    public ApiResponse<PostResponseDTO> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostDTO postDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        return ApiResponse.successRes(HttpStatus.OK, postService.updatePost(postId, postDTO, userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws AccessDeniedException {
        postService.deletePost(postId, userDetails.getUsername());
        return ApiResponse.successRes(HttpStatus.NO_CONTENT, null);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{postId}/like")
    public ApiResponse<PostResponseDTO> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername(); // Username 추출
        return ApiResponse.successRes(HttpStatus.OK, postService.likePost(postId, username));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{postId}/unlike")
    public ApiResponse<PostResponseDTO> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername(); // Username 추출
        return ApiResponse.successRes(HttpStatus.OK, postService.unlikePost(postId, username));
    }


}
