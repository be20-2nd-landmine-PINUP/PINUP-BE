package pinup.backend.ranking.common.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration // BEAN 등록 및 설정 담당
@EnableCaching// 스프링 캐시 기능 활성화; @Cacheable, @CacheEvict, @CachePut 등을 사용할 수 있게 함.
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Caffeine 기반 캐시 매니저 생성
        // "rankingTop100" 이라는 캐시 이름을 가진 캐시 공간 생성
        CaffeineCacheManager manager = new CaffeineCacheManager("rankingTop100");
        // Caffeine 캐시 설정
        manager.setCaffeine(Caffeine.newBuilder()
                // 캐시된 데이터는 1분위 자동 만료
                .expireAfterWrite(Duration.ofMinutes(1))
                // 캐시에 저장할 최대 항목 수 제한 (100개)
                .maximumSize(100));
        return manager;
    }
}
/*
기능 목적
Caffeine은 Spring Cache가 내부적으로 사용할 수 있는 고성능 메모리 캐시 라이브러리
즉, “DB 대신 빠르게 값을 꺼내기 위한 임시 저장소” 역할
이유: DB나 외부 API를 자주 호출하면 느려지니까,
최근 자주 쓰는 데이터를 메모리에 저장해두고 빠르게 응답하려는 목적
 */