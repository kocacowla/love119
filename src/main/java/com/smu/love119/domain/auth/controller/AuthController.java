package com.smu.love119.domain.auth.controller;
import com.smu.love119.domain.auth.dto.VerificationRequestDTO;
import com.smu.love119.domain.auth.dto.JoinDTO;
import com.smu.love119.domain.auth.service.AuthService;
import com.smu.love119.domain.auth.service.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final EmailVerificationService emailVerificationService;

    public AuthController(AuthService authService, EmailVerificationService emailVerificationService) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
    }


    // 회원가입 처리
    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        log.info("회원가입 요청: {}", joinDTO.getUsername());
        authService.joinProcess(joinDTO);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 관리자 회원가입 처리
    @PostMapping("/admin-join")
    public ResponseEntity<String> adminJoinProcess(@RequestBody JoinDTO joinDTO) {
        log.info("관리자 회원가입 요청: {}", joinDTO.getUsername());
        authService.adminJoinProcess(joinDTO);
        return ResponseEntity.ok("관리자 회원가입 성공");
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, String>> checkAvailability(
            @RequestParam("username") String username,
            @RequestParam(value = "nickname", required = false) String nickname) {

        log.info("중복 체크 요청: username={}, nickname={}", username, nickname);

        Map<String, String> response = new HashMap<>();

        // 기존의 authService 중복 체크 메서드를 활용하여 deletedDate가 null인 경우만 중복 확인
        boolean isUsernameExist = authService.isEmailDuplicated(username);
        boolean isNicknameExist = nickname != null && authService.isNicknameDuplicated(nickname);

        if (isUsernameExist) {
            response.put("emailError", "이미 존재하는 이메일입니다.");
        }
        if (isNicknameExist) {
            response.put("nicknameError", "이미 존재하는 닉네임입니다.");
        }

        if (response.isEmpty()) {
            return ResponseEntity.ok(response); // 중복 없음
        }

        return ResponseEntity.badRequest().body(response); // 중복 존재
    }



    // username으로 인증번호 발송
    @PostMapping("/join/emails/verification-requests")
    public ResponseEntity<String> sendVerificationCode(@RequestBody VerificationRequestDTO requestDTO) {
        emailVerificationService.sendVerificationCode(requestDTO.getUsername());
        return ResponseEntity.ok("인증번호가 발송되었습니다.");
    }

    // username으로 인증번호 확인
    @GetMapping("/join/emails/verification-requests")
    public ResponseEntity<String> verifyCode(@RequestParam("username") String username, @RequestParam("code") String code) {
        boolean isVerified = emailVerificationService.verifyCode(username, code);
        if (isVerified) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.badRequest().body("인증 실패");
        }
    }

}
