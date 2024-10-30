package com.smu.love119.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.smu.love119.domain.post.entity.Post.MBTI;

@Data
@Builder
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String postTitle;
    private String postContent;
    private MBTI mbti;
    private String nickname;
    private String time;
    private int likeCount;
    private boolean isLiked;
    private String myMbti;
    private String username;

}
