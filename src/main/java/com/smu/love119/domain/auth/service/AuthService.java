package com.smu.love119.domain.auth.service;

import com.smu.love119.domain.auth.dto.JoinDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {
        log.info("회원가입 요청: {}", joinDTO.getUsername());

        if (isEmailDuplicated(joinDTO.getUsername())) {
            log.warn("회원가입 실패 - 중복된 이메일: {}", joinDTO.getUsername());
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .username(joinDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .nickname(joinDTO.getNickname())
                .role(User.RoleType.ROLE_USER)
                .build();

        userRepository.save(user);
        log.info("회원가입 성공: {}", joinDTO.getUsername());
    }

    public boolean isEmailDuplicated(String username) {
        boolean exists = userRepository.existsByUsername(username);
        log.info("이메일 중복 체크: {}, 중복 여부: {}", username, exists);
        return exists;
    }

    public boolean isNicknameDuplicated(String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        log.info("닉네임 중복 체크: {}, 중복 여부: {}", nickname, exists);
        return exists;
    }
}
