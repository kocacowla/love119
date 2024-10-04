package com.smu.love119.domain.user.controller;

import com.smu.love119.domain.post.dto.PostCommentDto;
import com.smu.love119.domain.post.dto.PostDTO;
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

import java.util.List;

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

    // 마이페이지 게시물 조회
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/mypage/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@AuthenticationPrincipal UserDetails userDetails) {
        List<PostDTO> posts = userService.getUserPosts(userDetails.getUsername());
        return ResponseEntity.ok(posts);
    }

    // 마이페이지 댓글 조회
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/mypage/comments")
    public ResponseEntity<List<PostCommentDto>> getUserComments(@AuthenticationPrincipal UserDetails userDetails) {
        List<PostCommentDto> comments = userService.getUserComments(userDetails.getUsername());
        return ResponseEntity.ok(comments);
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

    // ROLE_USER 회원 조회 (GET) - 관리자 권한만
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllRoleUserMembers() {
        List<UserDTO> users = userService.getAllRoleUserMembers();
        return ResponseEntity.ok(users);
    }

    // 회원 강제탈퇴 (DELETE) - 관리자 권한만
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> adminDeleteUser(@PathVariable String username) {
        userService.adminDeleteUser(username);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content 응답
    }

    // 회원 복구 (POST) - 관리자 권한만
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/restore/{username}")
    public ResponseEntity<UserDTO> restoreUser(@PathVariable String username) {
        UserDTO restoredUser = userService.restoreUser(username);
        return ResponseEntity.ok(restoredUser);
    }


}