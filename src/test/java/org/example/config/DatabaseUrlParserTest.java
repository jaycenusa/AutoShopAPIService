package org.example.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseUrlParserTest {

    @Test
    void parsesRenderPostgresUrl() {
        DatabaseUrlParser.ParsedDatabaseUrl parsed = DatabaseUrlParser.parse(
                "postgres://autoshop:s3cret@dpg-abc.oregon-postgres.render.com:5432/autoshop"
        );

        assertThat(parsed.jdbcUrl())
                .isEqualTo("jdbc:postgresql://dpg-abc.oregon-postgres.render.com:5432/autoshop");
        assertThat(parsed.username()).isEqualTo("autoshop");
        assertThat(parsed.password()).isEqualTo("s3cret");
    }

    @Test
    void parsesPostgresqlSchemeWithQuery() {
        DatabaseUrlParser.ParsedDatabaseUrl parsed = DatabaseUrlParser.parse(
                "postgresql://user:pass@localhost:5432/autoshop?sslmode=require"
        );

        assertThat(parsed.jdbcUrl())
                .isEqualTo("jdbc:postgresql://localhost:5432/autoshop?sslmode=require");
        assertThat(parsed.username()).isEqualTo("user");
        assertThat(parsed.password()).isEqualTo("pass");
    }

    @Test
    void rejectsBlankUrl() {
        assertThatThrownBy(() -> DatabaseUrlParser.parse(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be blank");
    }

    @Test
    void rejectsMissingCredentials() {
        assertThatThrownBy(() -> DatabaseUrlParser.parse("postgres://localhost:5432/autoshop"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username and password");
    }
}
