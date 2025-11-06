package pinup.backend.point.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.time.Clock;

@Configuration
public class TimeConfig {

    @Bean
    @Primary
    public Clock systemClock() {
        // 시스템 기본 타임존 기준 Clock (애플리케이션 기본용)
        return Clock.systemDefaultZone();
    }
}
/*
Clock을 Bean으로 등록해, 애플리케이션 전역에서 현재 시각을 직접 new 하지 않고
Clock을 주입받아 사용하도록 만드는 설정
 */