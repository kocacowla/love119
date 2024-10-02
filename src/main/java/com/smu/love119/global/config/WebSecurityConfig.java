package com.smu.love119.global.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 모든 보안 설정을 비활성화 (개발 및 테스트 환경용)
        http.csrf().disable()
                .authorizeHttpRequests().anyRequest().permitAll();  // 모든 요청을 허용 (보안 비활성화)

        return http.build();
    }
}
