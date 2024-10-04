package com.smu.love119.domain.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;  // username 필드 사용
    private String code;  // 인증번호
    private LocalDateTime expirationTime;  // 만료 시간

    public VerificationCode(String username, String code, LocalDateTime expirationTime) {
        this.username = username;
        this.code = code;
        this.expirationTime = expirationTime;
    }

    // 기본 생성자 및 getter, setter 추가
    public VerificationCode() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}