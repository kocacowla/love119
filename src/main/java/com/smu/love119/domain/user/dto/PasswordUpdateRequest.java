package com.smu.love119.domain.user.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordUpdateRequest {
    @NotNull(message = "현재 비밀번호는 null이 될 수 없습니다.")
    private String currentPassword;

    @NotNull(message = "새로운 비밀번호는 null이 될 수 없습니다.")
    private String newPassword;
}