package com.smu.love119.domain.user.dto;

import com.smu.love119.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MbtiUpdateRequest {
    private User.MBTI myMbti;
    private User.MBTI favMbti;
}