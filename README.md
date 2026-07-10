# AutoShopAPIService

Backend API that supplies data for the [Auto Shop Inventory Management System](https://github.com/jaycenusa/auto-shop-inventory-management-system) frontend.

```
Frontend  →  Backend (this service)  →  Database (PostgreSQL)
```

## Objective

Provide a Spring Boot REST API that the frontend can call to read and write inventory and related shop data, with persistence in PostgreSQL.

## Tech stack

| Layer | Technology |
| --- | --- |
| Framework | Spring Boot 3.4.1 |
| Language | Java 17 |
| Web | Spring Web |
| Persistence | Spring Data JPA / Hibernate |
| Validation | Spring Validation |
| Database | PostgreSQL |
| Build | Gradle (Kotlin DSL) |
| Testing | Spring Boot Test, JUnit 5, Mockito |

## Prerequisites

- JDK 17+
- PostgreSQL running locally
- A database named `autoshop` (or update the JDBC URL)

## Configure

Local settings live in `src/main/resources/application-local.properties` (active via the `local` profile).

Defaults:

| Property | Default |
| --- | --- |
| JDBC URL | `jdbc:postgresql://localhost:5432/autoshop` |
| Username | `postgres` (override with `DB_USERNAME`) |
| Password | `postgres` (override with `DB_PASSWORD`) |

Optional environment overrides:

```bash
export DB_USERNAME=your_user
export DB_PASSWORD=your_password
```

Create the database if it does not exist:

```bash
createdb autoshop
# or in psql:
# CREATE DATABASE autoshop;
```

## Build

```bash
./gradlew build
```

## Run

```bash
./gradlew bootRun
```

The app starts with the `local` profile and connects to PostgreSQL using the configuration above. By default Spring Boot listens on port `8080`.

## Tests

```bash
./gradlew test
```

Tests also use the `local` profile.
