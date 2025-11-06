package pinup.backend.point.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class CoreSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/points/grant/like").hasRole("FEED")
                        .requestMatchers(HttpMethod.POST, "/api/points/grant/capture").hasRole("CAPTURE")
                        .requestMatchers(HttpMethod.POST, "/api/points/use").hasRole("STORE")
                        .requestMatchers(HttpMethod.GET,  "/api/points/**").authenticated()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}


