package com.smu.love119.domain.post.service;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.entity.PostComment;
import com.smu.love119.domain.post.mapper.PostCommentMapper;
import com.smu.love119.domain.post.repository.PostCommentRepository;
import com.smu.love119.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostCommentMapper postCommentMapper;

    // 댓글 조회
    public List<PostCommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));
        List<PostComment> comments = postCommentRepository.findAllByPost(post);
        return comments.stream()
                .map(postCommentMapper::toDTO)  // 여기를 수정
                .collect(Collectors.toList());
    }

    // 댓글 ID로 조회
    public PostCommentDto getCommentById(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        return postCommentMapper.toDTO(comment);
    }

    // 댓글 생성
    public PostCommentDto createComment(Long postId, PostCommentDto postCommentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));

        PostComment comment = postCommentMapper.toEntity(postCommentDto);  // 여기를 수정
        comment.setPost(post);

        PostComment savedComment = postCommentRepository.save(comment);
        return postCommentMapper.toDTO(savedComment);  // 여기를 수정
    }

    // 댓글 수정
    public PostCommentDto updateComment(Long commentId, PostCommentDto postCommentDTO) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));

        comment.setContent(postCommentDTO.getContent());
        comment.setLikeCount(postCommentDTO.getLikeCount());
        comment.setCommentClass(postCommentDTO.getCommentClass());

        PostComment updatedComment = postCommentRepository.save(comment);
        return postCommentMapper.toDTO(updatedComment);  // 여기를 수정
    }

    // 댓글 삭제
    public void deleteComment(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        postCommentRepository.delete(comment);
    }
}

