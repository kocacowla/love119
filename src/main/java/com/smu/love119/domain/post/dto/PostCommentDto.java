package com.smu.love119.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PostCommentDto {
    private Long id;
    private String content;
    private String nickname; // 유저 닉네임 추가
    private LocalDateTime createdDate; // 생성일자 추가
    private String myMbti;
    private String username;
}

