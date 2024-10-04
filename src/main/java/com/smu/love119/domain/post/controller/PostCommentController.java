package com.smu.love119.domain.post.controller;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.mapper.PostCommentMapper;
import com.smu.love119.domain.post.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PostCommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<PostCommentDto> comments = postCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{commentId}")
    public ResponseEntity<PostCommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        PostCommentDto comment = postCommentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<PostCommentDto> createComment(
            @PathVariable Long postId,
            @RequestBody PostCommentDto postCommentDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        PostCommentDto createdComment = postCommentService.createComment(postId, userDetails.getUsername(), postCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{commentId}")
    public ResponseEntity<PostCommentDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody PostCommentDto postCommentDto,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        postCommentService.verifyCommentAuthor(commentId, userDetails.getUsername());
        PostCommentDto updatedComment = postCommentService.updateComment(commentId, postCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) throws AccessDeniedException {
        postCommentService.verifyCommentAuthor(commentId, userDetails.getUsername());
        postCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{commentId}/like")
    public ResponseEntity<PostCommentDto> likeComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        PostCommentDto updatedComment = postCommentService.likeComment(commentId, userDetails.getUsername());
        return ResponseEntity.ok(updatedComment);
    }
}