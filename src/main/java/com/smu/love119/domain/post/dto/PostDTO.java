package com.smu.love119.domain.post.dto;

import com.smu.love119.domain.post.entity.Post.MBTI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private String postTitle;
    private String postContent;
    private MBTI mbti;
}