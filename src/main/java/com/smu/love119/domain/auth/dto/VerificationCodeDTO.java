package com.smu.love119.domain.auth.dto;

import lombok.Data;

@Data
public class VerificationCodeDTO {
    private String username;  // username 필드를 사용
    private String verificationCode;
}
