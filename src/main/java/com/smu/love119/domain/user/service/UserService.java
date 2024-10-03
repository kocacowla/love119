package com.smu.love119.domain.user.service;

import com.smu.love119.domain.user.dto.MbtiUpdateRequest;
import com.smu.love119.domain.user.dto.PasswordUpdateRequest;
import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.dto.MypageResponseDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.entity.User.MBTI;
import com.smu.love119.domain.user.mapper.UserMapper;
import com.smu.love119.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 비밀번호만 수정
    public void updatePassword(UserDetails userDetails, PasswordUpdateRequest passwordUpdateRequest) {
        if (passwordUpdateRequest.getCurrentPassword() == null || passwordUpdateRequest.getNewPassword() == null) {
            throw new IllegalArgumentException("Password fields cannot be null");
        }

        // 비밀번호 일치 여부 확인
        Optional<User> existingUser = userRepository.findByUsername(userDetails.getUsername());
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 현재 비밀번호 확인
            if (!bCryptPasswordEncoder.matches(passwordUpdateRequest.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid current password");
            }

            // 새 비밀번호로 업데이트
            User updatedUser = User.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .password(bCryptPasswordEncoder.encode(passwordUpdateRequest.getNewPassword()))  // 새로운 비밀번호 설정
                    .role(user.getRole())
                    .build();

            userRepository.save(updatedUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 회원 정보 조회
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(userMapper::toDTO).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // MBTI 수정
    public UserDTO updateUserMbti(String username, MbtiUpdateRequest mbtiUpdateRequest) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User existingUser = user.get();

            // Update user with new MBTI values
            User updatedUser = User.builder()
                    .id(existingUser.getId())
                    .username(existingUser.getUsername())
                    .nickname(existingUser.getNickname())
                    .password(existingUser.getPassword())
                    .myMbti(mbtiUpdateRequest.getMyMbti())
                    .favMbti(mbtiUpdateRequest.getFavMbti())
                    .role(existingUser.getRole())
                    .deletedDate(existingUser.getDeletedDate())
                    .build();

            return userMapper.toDTO(userRepository.save(updatedUser));
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 회원 탈퇴 (soft delete)
    public void deleteUser(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 탈퇴 처리: deletedDate 설정
            User updatedUser = User.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .password(user.getPassword()) // 비밀번호 유지
                    .myMbti(user.getMyMbti()) // 기존 MBTI 유지
                    .favMbti(user.getFavMbti()) // 기존 선호 MBTI 유지
                    .role(user.getRole()) // 기존 권한 유지
                    .deletedDate(LocalDateTime.now()) // 현재 시각을 탈퇴 시각으로 설정
                    .build();

            userRepository.save(updatedUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
/*
    // 회원 복구
    public UserDTO restoreUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User restoredUser = user.get();

            // Restore user by clearing the deleted date
            User updatedUser = User.builder()
                    .id(restoredUser.getId())
                    .username(restoredUser.getUsername())
                    .nickname(restoredUser.getNickname())
                    .password(restoredUser.getPassword())
                    .myMbti(restoredUser.getMyMbti())
                    .favMbti(restoredUser.getFavMbti())
                    .role(restoredUser.getRole())
                    .deletedDate(null)
                    .build();

            return userMapper.toDTO(userRepository.save(updatedUser));
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }*/
}
