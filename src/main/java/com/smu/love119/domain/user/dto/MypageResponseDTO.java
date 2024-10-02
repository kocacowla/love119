package com.smu.love119.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MypageResponseDTO {
    private String username;
    private String nickname;
    private String myMbti;
    private String favMbti;
    private int postCount; // 작성한 게시글 수
    private int commentCount; // 작성한 댓글 수
}
