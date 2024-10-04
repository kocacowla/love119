package com.smu.love119.domain.post.service;

import com.smu.love119.domain.post.dto.PostDTO;
import com.smu.love119.domain.post.dto.PostResponseDTO;
import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.mapper.PostMapper;
import com.smu.love119.domain.post.repository.PostRepository;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;
    private final Map<String, Boolean> userPostLikes = new HashMap<>();

    public PostService(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostResponseDTO createPost(String username, PostDTO postDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // Create Post entity with default values
        Post post = Post.builder()
                .user(user)
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .mbti(postDTO.getMbti())
                .viewCount(0) // default value
                .likeCount(0) // default value
                .deletedDate(null) // default value
                .build();

        Post savedPost = postRepository.save(post);
        return new PostResponseDTO(savedPost.getId(), savedPost.getPostTitle(), savedPost.getPostContent());
    }
    // 모든 게시글 조회
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAllByDeletedDateIsNull().stream()
                .map(postMapper::toResponseDTO)
                .collect(Collectors.toList());
    }




    public void verifyAuthor(Long postId, String username) throws AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        if (!post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("해당 게시글에 대한 권한이 없습니다.");
        }
    }
    @Transactional
    public PostResponseDTO updatePost(Long id, PostDTO postDTO, String username) throws AccessDeniedException {
        verifyAuthor(id, username); // 작성자 확인
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        post.setPostTitle(postDTO.getPostTitle());
        post.setPostContent(postDTO.getPostContent());
        Post updatedPost = postRepository.save(post);
        return postMapper.toResponseDTO(updatedPost);
    }

    @Transactional
    public void deletePost(Long id, String username) throws AccessDeniedException {
        verifyAuthor(id, username); // 작성자 확인
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // Soft delete by setting the deletedDate to the current timestamp
        post.setDeletedDate(LocalDateTime.now());
        postRepository.save(post); // Save changes to persist the soft delete
    }


    // 단일 게시글 조회
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findByIdAndDeletedDateIsNull(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return postMapper.toResponseDTO(post);
    }

    // 게시글 좋아요 추가
    @Transactional
    public PostResponseDTO likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + postId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (hasUserLikedPost(post, user)) {
            throw new IllegalStateException("이미 좋아요를 누르셨습니다.");
        }

        // 사용자 좋아요 상태 저장
        String userPostKey = generateUserPostKey(user.getId(), postId);
        userPostLikes.put(userPostKey, true);

        // 좋아요 수 증가
        post.setLikeCount(post.getLikeCount() + 1);
        Post updatedPost = postRepository.save(post);

        return postMapper.toResponseDTO(updatedPost);
    }

    // 사용자가 특정 게시글을 좋아요했는지 확인
    private boolean hasUserLikedPost(Post post, User user) {
        String userPostKey = generateUserPostKey(user.getId(), post.getId());
        return userPostLikes.getOrDefault(userPostKey, false);
    }

    // 사용자 ID와 게시글 ID를 기반으로 고유 키 생성
    private String generateUserPostKey(Long userId, Long postId) {
        return userId + "_" + postId;
    }


}
