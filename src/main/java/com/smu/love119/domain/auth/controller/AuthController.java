package com.smu.love119.domain.auth.controller;

import com.smu.love119.domain.auth.dto.JoinDTO;
import com.smu.love119.domain.auth.service.AuthService;
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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입 처리
    @PostMapping("/join")
    public ResponseEntity<String> joinProcess(@RequestBody JoinDTO joinDTO) {
        log.info("회원가입 요청: {}", joinDTO.getUsername());
        authService.joinProcess(joinDTO);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 이메일 및 닉네임 중복 체크
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, String>> checkAvailability(
            @RequestParam("username") String username,
            @RequestParam("nickname") String nickname) {

        log.info("중복 체크 요청: username={}, nickname={}", username, nickname);

        Map<String, String> response = new HashMap<>();
        boolean isUsernameExist = authService.isEmailDuplicated(username);
        boolean isNicknameExist = authService.isNicknameDuplicated(nickname);

        if (isUsernameExist) {
            response.put("emailError", "이미 존재하는 이메일입니다.");
        }
        if (isNicknameExist) {
            response.put("nicknameError", "이미 존재하는 닉네임입니다.");
        }

        if (response.isEmpty()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }
}
