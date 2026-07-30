package org.example.config;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Converts Render-style {@code postgres://} / {@code postgresql://} URLs into JDBC settings.
 */
public final class DatabaseUrlParser {

    public record ParsedDatabaseUrl(String jdbcUrl, String username, String password) {
    }

    private DatabaseUrlParser() {
    }

    public static ParsedDatabaseUrl parse(String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalArgumentException("DATABASE_URL must not be blank");
        }

        String normalized = databaseUrl.trim();
        if (normalized.startsWith("jdbc:")) {
            return parseJdbcUrl(normalized);
        }

        if (normalized.startsWith("postgres://")) {
            normalized = "postgresql://" + normalized.substring("postgres://".length());
        }
        if (!normalized.startsWith("postgresql://")) {
            throw new IllegalArgumentException("Unsupported DATABASE_URL scheme: " + databaseUrl);
        }

        try {
            URI uri = URI.create(normalized);
            String userInfo = uri.getUserInfo();
            if (userInfo == null || userInfo.isBlank()) {
                throw new IllegalArgumentException("DATABASE_URL must include username and password");
            }

            String username;
            String password;
            int colon = userInfo.indexOf(':');
            if (colon >= 0) {
                username = userInfo.substring(0, colon);
                password = userInfo.substring(colon + 1);
            } else {
                username = userInfo;
                password = "";
            }

            String path = uri.getPath();
            if (path == null || path.isBlank() || "/".equals(path)) {
                throw new IllegalArgumentException("DATABASE_URL must include a database name");
            }

            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + path;
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                jdbcUrl += "?" + uri.getQuery();
            }

            return new ParsedDatabaseUrl(jdbcUrl, username, password);
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid DATABASE_URL: " + databaseUrl, ex);
        }
    }

    private static ParsedDatabaseUrl parseJdbcUrl(String jdbcUrl) {
        // jdbc:postgresql://user:pass@host:port/db is uncommon; prefer host-only JDBC + separate creds.
        // If already jdbc and no userinfo, caller must supply username/password env vars.
        String withoutPrefix = jdbcUrl.substring("jdbc:".length());
        try {
            URI uri = new URI(withoutPrefix);
            if (uri.getUserInfo() != null && !uri.getUserInfo().isBlank()) {
                return parse("postgresql://" + withoutPrefix.replaceFirst("^postgresql://", ""));
            }
        } catch (URISyntaxException ignored) {
            // fall through
        }
        return new ParsedDatabaseUrl(jdbcUrl, null, null);
    }
}
