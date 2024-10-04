package com.smu.love119.domain.post.controller;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.mapper.PostCommentMapper;
import com.smu.love119.domain.post.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostCommentMapper postCommentMapper;

    @GetMapping
    public ResponseEntity<List<PostCommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<PostCommentDto> comments = postCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<PostCommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
        PostCommentDto comment = postCommentService.getCommentById(commentId);  // 수정된 부분
        return ResponseEntity.ok(comment);
    }

    @PostMapping
    public ResponseEntity<PostCommentDto> createComment(@PathVariable Long postId, @RequestBody PostCommentDto postCommentDto) {
        PostCommentDto createdComment = postCommentService.createComment(postId, postCommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<PostCommentDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody PostCommentDto postCommentDto) {
        PostCommentDto updatedComment = postCommentService.updateComment(commentId, postCommentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        postCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
