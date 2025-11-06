package pinup.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pinup.backend.auth.command.service.CustomAdminDetailsService;
import pinup.backend.auth.command.service.CustomOAuth2UserService;
import pinup.backend.member.command.domain.Admin;
import pinup.backend.member.command.repository.AdminRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAdminDetailsService customAdminDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 개발 중이라면 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**", "/api/notifications/stream")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                // 사용자: OAuth2 로그인
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/", true)
                )
                // 관리자: Form 로그인
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/home", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    // 관리자 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder encoder) {
        return args -> {
            if (adminRepository.findByName("admin").isEmpty()) {
                adminRepository.save(Admin.builder()
                        .name("admin")
                        .password(encoder.encode("1234"))
                        .status(Admin.Status.ACTIVE)
                        .build());
            }
        };
    }

    /*
    * sse 연결 수립 테스트를 위해 임시로 /sse/** 경로는 filterchain을 우회하도록 설정함
    * 추후 삭제 예정
    */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/sse/**");
    }

}
