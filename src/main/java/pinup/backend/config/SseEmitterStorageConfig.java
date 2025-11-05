package pinup.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SseEmitterStorageConfig {
    @Bean
    public Map<Integer, SseEmitter> sseEmitter() {
        return new ConcurrentHashMap<>();
    }
}
