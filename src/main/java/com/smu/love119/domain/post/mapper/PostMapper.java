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

    // Post 엔티티를 PostResponseDTO로 변환, isLiked 값을 인자로 받음
    public PostResponseDTO toResponseDTO(Post post, boolean isLiked) {
        return PostResponseDTO.builder()
                .id(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .mbti(post.getMbti())
                .nickname(post.getUser().getNickname()) // 작성자 이름 매핑
                .time(post.getCreatedDate().toString()) // 작성 시간 매핑
                .likeCount(post.getLikeCount())
                .isLiked(isLiked)  // 좋아요 여부 매핑
                .build();
    }
}