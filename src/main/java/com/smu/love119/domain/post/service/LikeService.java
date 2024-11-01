package com.smu.love119.domain.post.service;

import com.smu.love119.domain.post.entity.Post;
import com.smu.love119.domain.post.repository.UserPostLikeRepository;
import com.smu.love119.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.smu.love119.domain.post.repository.PostRepository;
import com.smu.love119.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserPostLikeRepository userPostLikeRepository;

    public boolean isPostLiked(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID: " + postId));

        return userPostLikeRepository.existsByUserAndPost(user, post);
    }
}
