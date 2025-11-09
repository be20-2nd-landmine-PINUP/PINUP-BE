package pinup.backend.store.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StoreOpenApiConfig {

    @Bean
    public GroupedOpenApi storeGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("store")
                .packagesToScan("pinup.backend.store")
                .build();
    }

}
