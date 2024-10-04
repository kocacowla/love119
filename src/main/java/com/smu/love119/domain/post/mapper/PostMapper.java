package com.smu.love119.domain.post.mapper;

import com.smu.love119.domain.post.dto.PostDTO;
import com.smu.love119.domain.post.dto.PostResponseDTO;
import com.smu.love119.domain.post.entity.Post;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final PostCommentMapper postCommentMapper;

    @Autowired
    public PostMapper(PostCommentMapper postCommentMapper) {
        this.postCommentMapper = postCommentMapper;
    }

    // Only the fields included in PostDTO are mapped
    public Post toEntity(PostDTO postDTO) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .mbti(postDTO.getMbti())
                .build();
    }

    // PostDTO still includes optional fields like comments and deletedDate
    public PostDTO toDTO(Post post) {
        return PostDTO.builder()
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .mbti(post.getMbti())
                .build();
    }

    // For the response, the mapping remains the same as before
    public PostResponseDTO toResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .build();
    }
}
