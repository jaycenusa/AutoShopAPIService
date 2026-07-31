package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autoShopOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoShop API")
                        .version("1.0")
                        .description("Inventory, customers, and reorders"));
    }

    @Bean
    public OpenApiCustomizer hideDeleteOperationsCustomizer() {
        return new HideDeleteOperationsCustomizer();
    }
}
