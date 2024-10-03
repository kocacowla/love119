package com.smu.love119.domain.user.mapper;

import com.smu.love119.domain.user.dto.UserDTO;
import com.smu.love119.domain.user.dto.MypageResponseDTO;
import com.smu.love119.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // DTO -> Entity 변환
    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .nickname(userDTO.getNickname())
                .password(userDTO.getPassword())
                .myMbti(User.MBTI.valueOf(userDTO.getMyMbti())) // String을 Enum으로 변환
                .favMbti(User.MBTI.valueOf(userDTO.getFavMbti())) // String을 Enum으로 변환
                .role(User.RoleType.valueOf(userDTO.getRole()))
                .build();
    }

    // 기존 Entity를 업데이트
    public User toEntity(UserDTO userDTO, User user) {
        return User.builder()
                .id(user.getId()) // 기존 ID 유지
                .username(user.getUsername()) // 기존 username 유지
                .nickname(userDTO.getNickname() != null ? userDTO.getNickname() : user.getNickname()) // 새 값이 없으면 기존 값 유지
                .password(userDTO.getPassword() != null ? userDTO.getPassword() : user.getPassword()) // 새 값이 없으면 기존 값 유지
                .myMbti(userDTO.getMyMbti() != null ? User.MBTI.valueOf(userDTO.getMyMbti()) : user.getMyMbti()) // String을 Enum으로 변환
                .favMbti(userDTO.getFavMbti() != null ? User.MBTI.valueOf(userDTO.getFavMbti()) : user.getFavMbti()) // String을 Enum으로 변환
                .deletedDate(user.getDeletedDate()) // 기존 deletedDate 유지
                .role(user.getRole()) // 기존 role 유지
                .build();
    }

    // Entity -> DTO 변환
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .password(user.getPassword())
                .myMbti(user.getMyMbti() != null ? user.getMyMbti().name() : null) // null 체크 추가
                .favMbti(user.getFavMbti() != null ? user.getFavMbti().name() : null)
                .role(user.getRole().name())
                .build();
    }

    // 마이페이지 응답 DTO로 변환
    public MypageResponseDTO toMypageResponseDTO(User user) {
        return MypageResponseDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .myMbti(user.getMyMbti().name()) // Enum을 String으로 변환
                .favMbti(user.getFavMbti() != null ? user.getFavMbti().name() : null) // Enum을 String으로 변환
                .build();
    }
}
