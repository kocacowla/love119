package com.smu.love119.domain.user.controller;

import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.dto.MypageResponseDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 회원 정보 수정 (PUT)
    @PutMapping("/join")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 정보 조회 (GET)
    @GetMapping("/mypage/{username}/info")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // 회원 탈퇴 (DELETE)
    @DeleteMapping("/mypage/{username}/delete")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    // 마이페이지 조회 (GET)
    @GetMapping("/mypage/{username}")
    public ResponseEntity<MypageResponseDTO> getMyPage(@PathVariable String username) {
        MypageResponseDTO myPage = userService.getMyPage(username);
        return ResponseEntity.ok(myPage);
    }

    // 회원 복구 (POST)
    @PostMapping("/restoreUser/{username}")
    public ResponseEntity<UserDTO> restoreUser(@PathVariable String username) {
        UserDTO restoredUser = userService.restoreUser(username);
        return ResponseEntity.ok(restoredUser);
    }

    // MBTI 입력/수정 (PUT)
    @PutMapping("/mymbti/{username}")
    public ResponseEntity<UserDTO> updateMbti(@PathVariable String username,
                                              @RequestParam User.MBTI myMbti,
                                              @RequestParam User.MBTI favMbti) {
        UserDTO updatedUser = userService.updateUserMbti(username, myMbti, favMbti);
        return ResponseEntity.ok(updatedUser);
    }
}
