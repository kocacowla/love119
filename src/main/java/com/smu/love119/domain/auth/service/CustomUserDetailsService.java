package com.smu.love119.domain.auth.service;

import com.smu.love119.domain.auth.dto.CustomUserDetailsDTO;
import com.smu.love119.domain.user.entity.User;
import com.smu.love119.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 조회
        Optional<User> userData = userRepository.findByUsername(username);

        if (userData.isPresent()) { // Optional에 값이 있는지 확인
            // UserDetails에 담아 return하면 AuthenticationManager가 검증
            return new CustomUserDetailsDTO(userData.get()); // Optional에서 User 객체 꺼내기
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

}