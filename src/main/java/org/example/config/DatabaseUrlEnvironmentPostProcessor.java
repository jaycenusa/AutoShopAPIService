package org.example.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps Render {@code DATABASE_URL} (postgres://...) into Spring datasource properties.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }

        String existingJdbcUrl = environment.getProperty("spring.datasource.url");
        if (existingJdbcUrl != null && existingJdbcUrl.startsWith("jdbc:")) {
            return;
        }

        DatabaseUrlParser.ParsedDatabaseUrl parsed = DatabaseUrlParser.parse(databaseUrl);
        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.datasource.url", parsed.jdbcUrl());
        if (parsed.username() != null) {
            properties.put("spring.datasource.username", parsed.username());
        }
        if (parsed.password() != null) {
            properties.put("spring.datasource.password", parsed.password());
        }
        properties.put("spring.datasource.driver-class-name", "org.postgresql.Driver");

        environment.getPropertySources().addFirst(new MapPropertySource("renderDatabaseUrl", properties));
    }
}
