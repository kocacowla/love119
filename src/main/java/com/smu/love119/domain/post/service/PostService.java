package com.smu.love119.domain.post.service;

import com.smu.love119.domain.post.dto.PostDTO;
import com.smu.love119.domain.post.dto.PostResponseDTO;
import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.entity.UserPostLike;
import com.smu.love119.domain.chatbot.service.ChatbotService;
import com.smu.love119.domain.post.mapper.PostMapper;
import com.smu.love119.domain.post.repository.PostRepository;
import com.smu.love119.domain.post.repository.UserPostLikeRepository;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private static final int PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    private final ChatbotService chatbotService;

    private final UserPostLikeRepository userPostLikeRepository;
    private final Map<String, Boolean> userPostLikes = new HashMap<>();

    public PostService(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository, UserPostLikeRepository userPostLikeRepository, ChatbotService chatbotService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
        this.userPostLikeRepository = userPostLikeRepository;
        this.chatbotService = chatbotService;
    }

    // 게시글 생성
    @Transactional
    public PostResponseDTO createPost(String username, PostDTO postDTO) {
        User user = getUserByUsername(username);
        postDTO.setMyMbti(user.getMyMbti().name());

        // ChatGPT 조언 생성
        String advice = chatbotService.generateAdviceForPost(
                postDTO.getPostTitle(),
                postDTO.getPostContent(),
                postDTO.getMbti().name(),
                postDTO.getMyMbti()
        );

        // Post 생성 및 저장
        Post post = Post.builder()
                .user(user)
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .mbti(postDTO.getMbti())
                .viewCount(0)
                .likeCount(0)
                .advice(advice) // 생성된 advice 설정
                .build();
        Post savedPost = postRepository.save(post);

        // advice 포함된 PostResponseDTO 반환
        return postMapper.toResponseDTO(savedPost, false, advice); // advice 전달
    }


    // 단일 게시글 조회 (좋아요 여부 포함)
    @Transactional
    public PostResponseDTO getPostById(Long postId, String username) {
        Post post = findActivePostById(postId);
        User user = getUserByUsername(username);
        boolean isLiked = hasUserLikedPost(post, user);

        // 저장된 advice를 사용하여 반환
        return postMapper.toResponseDTO(post, isLiked, post.getAdvice());
    }

    // 최신 게시글 조회
    public List<PostResponseDTO> getLatestPosts(int page) {
        return getPagedPosts(page, Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    // 인기 게시글 조회
    public List<PostResponseDTO> getPopularPosts(int page) {
        return getPagedPosts(page, Sort.by(Sort.Direction.DESC, "likeCount"));
    }

    // 키워드로 게시글 검색
    public List<PostResponseDTO> searchPosts(String keyword, int page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> posts = postRepository.searchByKeyword(keyword, pageRequest);
        return posts.getContent().stream()
                .map(post -> postMapper.toResponseDTO(post, false))
                .collect(Collectors.toList());
    }



    @Transactional
    public PostResponseDTO likePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (postRepository.hasUserLikedPost(user.getId(), post.getId())) {
            throw new RuntimeException("Already liked this post");
        }

        UserPostLike like = UserPostLike.builder()
                .user(user)
                .post(post)
                .build();
        userPostLikeRepository.save(like);

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return postMapper.toResponseDTO(post, true);
    }

    @Transactional
    public PostResponseDTO unlikePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        UserPostLike like = userPostLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        userPostLikeRepository.delete(like);

        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);

        return postMapper.toResponseDTO(post, false);
    }
    // 게시글 수정
    @Transactional
    public PostResponseDTO updatePost(Long id, PostDTO postDTO, String username) throws AccessDeniedException {
        verifyAuthor(id, username);  // 작성자 확인
        Post post = findActivePostById(id);

        post.setPostTitle(postDTO.getPostTitle());
        post.setPostContent(postDTO.getPostContent());

        Post updatedPost = postRepository.save(post);
        return postMapper.toResponseDTO(updatedPost, false);  // 수정 후 좋아요 상태는 유지하지 않음
    }

    // 게시글 삭제 (소프트 삭제)
    @Transactional
    public void deletePost(Long id, String username) throws AccessDeniedException {
        verifyAuthor(id, username);
        Post post = findActivePostById(id);

        post.setDeletedDate(LocalDateTime.now());
        postRepository.save(post);
    }

    // 작성자 확인
    public void verifyAuthor(Long postId, String username) throws AccessDeniedException {
        Post post = findActivePostById(postId);
        if (!post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("해당 게시글에 대한 권한이 없습니다.");
        }
    }

    // 좋아요 상태 갱신 (추가/취소)
//    private void updateLikeState(Post post, User user, boolean isLiked) {
//        String key = generateUserPostKey(user.getId(), post.getId());
//
//        if (isLiked) {
//            userPostLikes.put(key, true);
//            post.setLikeCount(post.getLikeCount() + 1);
//        } else {
//            userPostLikes.remove(key);
//            post.setLikeCount(post.getLikeCount() - 1);
//        }
//
//        postRepository.save(post);
//    }

    // 삭제되지 않은 게시글 조회
    private Post findActivePostById(Long postId) {
        return postRepository.findByIdAndDeletedDateIsNull(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    // 사용자 조회
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 특정 페이지의 게시글 조회
    private List<PostResponseDTO> getPagedPosts(int page, Sort sort) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, sort);
        Page<Post> posts = postRepository.findAllByDeletedDateIsNull(pageRequest);
        return posts.getContent().stream()
                .map(post -> postMapper.toResponseDTO(post, false))
                .collect(Collectors.toList());
    }

    // 사용자가 게시글을 좋아요했는지 여부 확인
    private boolean hasUserLikedPost(Post post, User user) {
        String key = generateUserPostKey(user.getId(), post.getId());
        return userPostLikes.getOrDefault(key, false);
    }

    // 사용자-게시글 고유 키 생성
    private String generateUserPostKey(Long userId, Long postId) {
        return userId + "_" + postId;
    }
}
