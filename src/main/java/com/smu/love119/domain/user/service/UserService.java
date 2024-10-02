package com.smu.love119.domain.user.service;

import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.dto.MypageResponseDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.entity.User.MBTI;
import com.smu.love119.domain.user.mapper.UserMapper;
import com.smu.love119.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // 회원가입
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO); // DTO를 Entity로 변환
        return userMapper.toDTO(userRepository.save(user)); // Entity 저장 후 DTO로 반환
    }

    // 회원 정보 수정
    public UserDTO updateUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(userDTO.getId());
        if (existingUser.isPresent()) {
            User updatedUser = userMapper.toEntity(userDTO, existingUser.get()); // 기존 Entity에 업데이트
            return userMapper.toDTO(userRepository.save(updatedUser)); // 저장 후 DTO로 반환
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 회원 정보 조회
    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username); // Optional<User>로 받아야 함
        if (user.isPresent()) {
            return userMapper.toDTO(user.get()); // Optional에서 User를 꺼내 사용
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 회원 탈퇴 (삭제)
    public void deleteUser(String username) {
        Optional<User> user = userRepository.findByUsername(username); // Optional<User>로 받아야 함
        if (user.isPresent()) {
            userRepository.delete(user.get()); // Optional에서 User를 꺼내 삭제
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // 회원 복구
    public UserDTO restoreUser(String username) {
        Optional<User> user = userRepository.findByUsername(username); // Optional<User>로 받아야 함
        if (user.isPresent()) {
            User restoredUser = user.get();
            restoredUser = User.builder()
                    .id(restoredUser.getId())
                    .username(restoredUser.getUsername())
                    .nickname(restoredUser.getNickname())
                    .password(restoredUser.getPassword())
                    .myMbti(restoredUser.getMyMbti())
                    .favMbti(restoredUser.getFavMbti())
                    .role(restoredUser.getRole())
                    .deletedDate(null) // 복구 시 삭제 날짜를 null로 설정
                    .build();
            return userMapper.toDTO(userRepository.save(restoredUser));
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // MBTI 수정
    public UserDTO updateUserMbti(String username, User.MBTI myMbti, User.MBTI favMbti) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser = User.builder()
                    .id(existingUser.getId())
                    .username(existingUser.getUsername())
                    .nickname(existingUser.getNickname())
                    .password(existingUser.getPassword())
                    .myMbti(myMbti) // myMbti 수정
                    .favMbti(favMbti) // favMbti 수정
                    .role(existingUser.getRole())
                    .deletedDate(existingUser.getDeletedDate())
                    .build();
            return userMapper.toDTO(userRepository.save(existingUser));
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


    // 마이페이지 정보 조회
    public MypageResponseDTO getMyPage(String username) {
        Optional<User> user = userRepository.findByUsername(username); // Optional<User>로 받아야 함
        if (user.isPresent()) {
            return userMapper.toMypageResponseDTO(user.get()); // Optional에서 User를 꺼내 사용
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
