package com.smu.love119.domain.post.service;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.entity.PostComment;
import com.smu.love119.domain.post.mapper.PostCommentMapper;
import com.smu.love119.domain.post.repository.PostCommentRepository;
import com.smu.love119.domain.post.repository.PostRepository;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostCommentMapper postCommentMapper;
    private final UserRepository userRepository; // UserRepository 주입

    // 좋아요 상태를 저장하는 Map (userId와 commentId를 결합한 문자열을 키로 사용)
    private final Map<String, Boolean> userCommentLikes = new ConcurrentHashMap<>();

    // 댓글 조회
    public List<PostCommentDto> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));
        List<PostComment> comments = postCommentRepository.findAllByPost(post)
                .stream()
                .filter(comment -> comment.getDeletedDate() == null) // Soft delete된 댓글 제외
                .collect(Collectors.toList());
        return comments.stream()
                .map(postCommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 댓글 ID로 조회
    public PostCommentDto getCommentById(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .filter(c -> c.getDeletedDate() == null) // Soft delete된 댓글 제외
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));
        return postCommentMapper.toDTO(comment);
    }

    // 댓글 생성
    public PostCommentDto createComment(Long postId, String username, PostCommentDto postCommentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id " + postId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username " + username));

        PostComment comment = PostComment.builder()
                .post(post)
                .user(user)
                .content(postCommentDto.getContent())
                .build();

        PostComment savedComment = postCommentRepository.save(comment);
        return postCommentMapper.toDTO(savedComment);
    }


    // 댓글 작성자 검증
    public void verifyCommentAuthor(Long commentId, String username) throws AccessDeniedException {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("댓글에 대한 권한이 없습니다.");
        }
    }

    // 댓글 수정
    public PostCommentDto updateComment(Long commentId, PostCommentDto postCommentDto) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));

        // Directly update the fields of the existing entity
        comment.setContent(postCommentDto.getContent());

        // Save the updated entity
        postCommentRepository.save(comment);

        // Return the DTO
        return postCommentMapper.toDTO(comment);
    }


    // 댓글 소프트 삭제
    public void deleteComment(Long commentId) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + commentId));

        // deletedDate 필드에 현재 시간 설정
        comment.setDeletedDate(LocalDateTime.now());

        // 변경 사항 저장 (삭제하지 않음)
        postCommentRepository.save(comment);
    }

    // 댓글 좋아요 추가 메서드
    @Transactional
    public PostCommentDto likeComment(Long commentId, String username) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (hasUserLikedComment(comment, user)) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        // 좋아요 상태 업데이트
        String userCommentKey = generateUserCommentKey(user.getId(), commentId);
        userCommentLikes.put(userCommentKey, true);

        // likeCount 직접 수정
        comment.setLikeCount(comment.getLikeCount() + 1);

        // 변경 사항 저장
        postCommentRepository.save(comment);
        return postCommentMapper.toDTO(comment);
    }

    // 좋아요 여부 확인 메서드
    private boolean hasUserLikedComment(PostComment comment, User user) {
        String userCommentKey = generateUserCommentKey(user.getId(), comment.getId());
        return userCommentLikes.getOrDefault(userCommentKey, false);
    }

    // userId와 commentId를 조합하여 고유 키 생성
    private String generateUserCommentKey(Long userId, Long commentId) {
        return userId + "_" + commentId;
    }
}