import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 비활성화
                .authorizeRequests()
                .requestMatchers("/api/**").authenticated() // 인증이 필요한 경로 설정
                .anyRequest().permitAll() // 나머지 경로는 인증 불필요
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
                .and()
                .httpBasic().disable(); // Basic Authentication 비활성화

        return http.build(); // SecurityFilterCShain 객체 반환
    }
}
