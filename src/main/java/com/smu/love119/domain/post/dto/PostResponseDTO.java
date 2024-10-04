package com.smu.love119.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String postTitle;
    private String postContent;
}
