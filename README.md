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
| Testing | Spring Boot Test, JUnit 5, Mockito, H2 |

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

```bash
export DB_USERNAME=your_user
export DB_PASSWORD=your_password
createdb autoshop
```

On first startup with an empty database, seed data matching the frontend mocks is loaded (parts, customers, reorders).

## Build

```bash
./gradlew build
```

## Run

```bash
./gradlew bootRun
```

Listens on port `8080` with the `local` profile.

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/health` | Health check |
| GET | `/api/parts` | List parts (`category`, `search` query params) |
| GET | `/api/parts/{id}` | Get part |
| POST | `/api/parts` | Create part |
| PUT | `/api/parts/{id}` | Update part |
| DELETE | `/api/parts/{id}` | Delete part |
| GET | `/api/customers` | List/search (`q`, `status`) |
| GET | `/api/customers/stats` | Customer aggregates |
| GET | `/api/customers/{id}` | Get customer |
| POST | `/api/customers` | Create customer |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |
| GET | `/api/reorders` | List reorders (`status`) |
| GET | `/api/reorders/{id}` | Get reorder |
| POST | `/api/reorders` | Create reorder |
| PATCH | `/api/reorders/{id}/status` | Update status (`pending` / `ordered` / `delivered` / `cancelled`) |
| DELETE | `/api/reorders/{id}` | Delete reorder |

Marking a reorder as `delivered` increments the related part's stock.

## Swagger UI

Interactive API docs (DELETE operations are omitted from the published spec):

| Resource | URL |
| --- | --- |
| Swagger UI | https://autoshopapiservice.onrender.com/swagger-ui.html |
| OpenAPI JSON | https://autoshopapiservice.onrender.com/v3/api-docs |

## Tests

```bash
./gradlew test
```

Tests use the `test` profile with an in-memory H2 database (PostgreSQL not required).

## Container Deployment

This service ships with a multi-stage [`Dockerfile`](Dockerfile) that builds a runnable JAR and runs it on the `prod` profile.

### Build the image

```bash
docker build -t autoshop-api .
```

### Run the container

Provide a PostgreSQL connection via `DATABASE_URL` (`postgres://` or `postgresql://`). The app maps that URL into Spring datasource settings at startup.

```bash
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=postgres://user:pass@host:5432/autoshop \
  autoshop-api
```

If Postgres is also running in Docker on the same machine, use the container/service hostname (or `host.docker.internal` on Docker Desktop) instead of `localhost`.

### Verify

```bash
curl https://autoshopapiservice.onrender.com/api/health
```
