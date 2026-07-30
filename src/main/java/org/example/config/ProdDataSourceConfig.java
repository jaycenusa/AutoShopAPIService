package org.example.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Fallback DataSource bean when Render injects {@code DATABASE_URL} at runtime.
 */
@Configuration
@Profile("prod")
public class ProdDataSourceConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "DATABASE_URL")
    public DataSource dataSource(Environment environment) {
        String databaseUrl = environment.getRequiredProperty("DATABASE_URL");
        DatabaseUrlParser.ParsedDatabaseUrl parsed = DatabaseUrlParser.parse(databaseUrl);

        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(parsed.jdbcUrl())
                .username(parsed.username())
                .password(parsed.password())
                .build();
    }
}
