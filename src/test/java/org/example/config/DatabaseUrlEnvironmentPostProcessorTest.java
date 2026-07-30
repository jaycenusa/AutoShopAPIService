package org.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseUrlEnvironmentPostProcessorTest {

    private final DatabaseUrlEnvironmentPostProcessor processor = new DatabaseUrlEnvironmentPostProcessor();

    @Test
    void mapsDatabaseUrlIntoSpringDatasourceProperties() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty(
                "DATABASE_URL",
                "postgres://autoshop:s3cret@dpg-abc.oregon-postgres.render.com:5432/autoshop"
        );

        processor.postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://dpg-abc.oregon-postgres.render.com:5432/autoshop");
        assertThat(environment.getProperty("spring.datasource.username")).isEqualTo("autoshop");
        assertThat(environment.getProperty("spring.datasource.password")).isEqualTo("s3cret");
        assertThat(environment.getProperty("spring.datasource.driver-class-name"))
                .isEqualTo("org.postgresql.Driver");
    }

    @Test
    void overridesEmptyDatasourceUrlPlaceholder() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("spring.datasource.url", "");
        environment.setProperty("DATABASE_URL", "postgres://autoshop:s3cret@host:5432/autoshop");

        processor.postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://host:5432/autoshop");
    }

    @Test
    void ignoresMissingDatabaseUrl() {
        MockEnvironment environment = new MockEnvironment();

        processor.postProcessEnvironment(environment, new SpringApplication());

        assertThat(environment.getProperty("spring.datasource.url")).isNull();
    }
}
