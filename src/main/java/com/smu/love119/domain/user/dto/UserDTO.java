package com.smu.love119.domain.user.dto;

import com.smu.love119.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String myMbti; // String 타입으로 유지
    private String favMbti; // String 타입으로 유지
    private String role;
}
