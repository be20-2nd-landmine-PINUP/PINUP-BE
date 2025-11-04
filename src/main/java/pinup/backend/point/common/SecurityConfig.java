package pinup.backend.point.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // @PreAuthorize 활성화
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API면 보통 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/points/use").hasRole("STORE")
                        .requestMatchers(HttpMethod.POST, "/api/points/grant").hasAnyRole("FEED", "CAPTURE")
                        .anyRequest().authenticated()
                );
        // JWT 쓰면 여기서 추가 필터/설정 연결(예: http.oauth2ResourceServer(oauth2 -> oauth2.jwt(...)))
        // 시간 되면 추가 수정 사항.
        return http.build();
    }
}
/*
목적: 권한 제어 용도.
 */