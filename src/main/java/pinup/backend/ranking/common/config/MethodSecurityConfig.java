package pinup.backend.ranking.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration // 이 클래스가 보안 관련 설정을 담고 있음
@EnableMethodSecurity// 메서드 보환 활성화
public class MethodSecurityConfig {
}
//