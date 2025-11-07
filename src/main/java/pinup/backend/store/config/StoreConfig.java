package pinup.backend.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StoreConfig {

    @Bean
    public RestTemplate storeRestemplate() {
        return new RestTemplate();
    }
}

