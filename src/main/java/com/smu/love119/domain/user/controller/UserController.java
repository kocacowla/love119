package com.smu.love119.domain.user.controller;

import com.smu.love119.domain.user.dto.MbtiUpdateRequest;
import com.smu.love119.domain.user.dto.PasswordUpdateRequest;
import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.dto.MypageResponseDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 비밀번호 수정 (PUT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                                               @RequestBody @Valid PasswordUpdateRequest passwordUpdateRequest) {
        userService.updatePassword(userDetails, passwordUpdateRequest);
        return ResponseEntity.ok().build();
    }

    // 회원 정보 조회 (GET)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/mypage")
    public ResponseEntity<UserDTO> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    // MBTI 입력/수정 (PUT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/mymbti")
    public ResponseEntity<UserDTO> updateMbti(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestBody MbtiUpdateRequest mbtiUpdateRequest) {
        UserDTO updatedUser = userService.updateUserMbti(userDetails.getUsername(), mbtiUpdateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 탈퇴 (DELETE)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/mypage/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());
        return ResponseEntity.noContent().build(); // HTTP 204 No Content 응답
    }
    /*
    // 회원 복구 (POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/restoreUser")
    public ResponseEntity<UserDTO> restoreUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO restoredUser = userService.restoreUser(userDetails.getUsername());
        return ResponseEntity.ok(restoredUser);
    }

     */
}