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
    private int likeCount;
    private int commentClass;
    private LocalDateTime deletedDate;
}

