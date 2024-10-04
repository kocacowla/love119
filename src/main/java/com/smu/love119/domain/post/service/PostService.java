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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

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
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
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


    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return postMapper.toResponseDTO(post);
    }
}
