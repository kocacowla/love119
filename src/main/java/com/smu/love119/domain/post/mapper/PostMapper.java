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

    // Post 엔티티를 PostResponseDTO로 변환, isLiked 포함
    public PostResponseDTO toResponseDTO(Post post, boolean isLiked, String advice) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .mbti(post.getMbti())  // 게시글 관련 MBTI 매핑
                .myMbti(post.getUser().getMyMbti() != null ? post.getUser().getMyMbti().name() : "Unknown")   // 작성자의 MBTI 매핑
                .nickname(post.getUser().getNickname())  // 작성자 닉네임 매핑
                .username(post.getUser().getUsername())
                .time(post.getCreatedDate().toString())  // 작성 시간 매핑
                .likeCount(post.getLikeCount())  // 좋아요 수 매핑
                .isLiked(isLiked)  // 좋아요 여부 매핑
                .advice(advice)
                .build();
    }
    public PostResponseDTO toResponseDTO(Post post, boolean isLiked) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .mbti(post.getMbti())
                .myMbti(post.getUser().getMyMbti() != null ? post.getUser().getMyMbti().name() : "Unknown")
                .nickname(post.getUser().getNickname())
                .username(post.getUser().getUsername())
                .time(post.getCreatedDate().toString())
                .likeCount(post.getLikeCount())
                .isLiked(isLiked)
                .build();
    }
}