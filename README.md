# Transaction API

A RESTful API built with **Spring Boot** for managing financial transactions and computing real-time statistics. Developed as a solution to the [Itaú Junior Developer Challenge (Vaga 99)](https://github.com/rafaellins-itau/desafio-itau-vaga-99-junior).

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Request & Response Models](#request--response-models)
- [Validation Rules](#validation-rules)
- [Running the Application](#running-the-application)
- [Running with Docker](#running-with-docker)
- [Running Tests](#running-tests)
- [Actuator & Monitoring](#actuator--monitoring)
- [API Documentation (Swagger)](#api-documentation-swagger)

---

## Overview

The Transaction API allows clients to:

- **Submit** financial transactions (value + timestamp).
- **Delete** all stored transactions from memory.
- **Query** real-time statistics (count, sum, average, min, max) for transactions within a configurable time window.

> **No database is used.** All transactions are stored in-memory using an `ArrayList`, as required by the challenge specification.

---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.0.3 |
| Spring Web MVC | (via Spring Boot) |
| Spring Boot Actuator | (via Spring Boot) |
| Lombok | Latest |
| SpringDoc OpenAPI (Swagger) | 3.0.2 |
| JUnit 5 + Mockito | (via Spring Boot Test) |
| Gradle (Kotlin DSL) | 9.3.1 |
| Docker | (OpenJDK 21 slim base) |

---

## Project Structure

```
transaction-api/
├── src/
│   ├── main/
│   │   ├── java/com/rafael/transaction_api/
│   │   │   ├── TransactionApiApplication.java          # Application entry point
│   │   │   ├── business/
│   │   │   │   └── services/
│   │   │   │       ├── TransactionService.java         # Transaction business logic
│   │   │   │       └── StatisticsService.java          # Statistics calculation logic
│   │   │   ├── controller/
│   │   │   │   ├── TransactionController.java          # POST /transaction, DELETE /transaction
│   │   │   │   ├── StatisticsController.java           # GET /statistics
│   │   │   │   ├── GlobalExceptionHandler.java         # Centralized error handling
│   │   │   │   └── dtos/
│   │   │   │       ├── TransactionRequestDTO.java      # Input model
│   │   │   │       └── StatisticsResponseDTO.java      # Output model
│   │   │   └── infrastructure/
│   │   │       └── exceptions/
│   │   │           └── UnprocessableEntity.java        # Custom 422 exception
│   │   └── resources/
│   │       └── application.yml                         # Application configuration
│   └── test/
│       └── java/com/rafael/transaction_api/
│           └── business/service/
│               ├── TransactionServiceTest.java
│               ├── TransactionControllerTest.java
│               ├── StatisticsServiceTest.java
│               └── StatisticsControllerTest.java
├── Dockerfile
├── build.gradle.kts
└── settings.gradle.kts
```

---

## API Endpoints

### `POST /transaction`

Adds a new transaction to in-memory storage.

**Request Body:**
```json
{
  "value": 123.45,
  "dateTime": "2026-03-10T14:30:00Z"
}
```

**Responses:**

| Status | Description |
|---|---|
| `201 Created` | Transaction recorded successfully |
| `422 Unprocessable Entity` | Value is negative or `dateTime` is in the future |
| `400 Bad Request` | Malformed request body |
| `500 Internal Server Error` | Unexpected server error |

---

### `DELETE /transaction`

Clears all transactions from memory.

**Responses:**

| Status | Description |
|---|---|
| `200 OK` | All transactions deleted |
| `400 Bad Request` | Request error |
| `500 Internal Server Error` | Unexpected server error |

---

### `GET /statistics?rangeSearch={seconds}`

Returns computed statistics for all transactions recorded within the last `rangeSearch` seconds.

**Query Parameter:**

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `rangeSearch` | Integer | No | `60` | Time window in seconds to filter transactions |

**Response Body:**
```json
{
  "count": 3,
  "sum": 350.75,
  "avg": 116.92,
  "min": 50.00,
  "max": 200.75
}
```

> If there are no transactions within the window, all fields return `0`.

**Responses:**

| Status | Description |
|---|---|
| `200 OK` | Statistics returned successfully |
| `400 Bad Request` | Error in the request |
| `500 Internal Server Error` | Unexpected server error |

---

## Request & Response Models

### `TransactionRequestDTO`

```java
public record TransactionRequestDTO(Double value, OffsetDateTime dateTime) {}
```

| Field | Type | Description |
|---|---|---|
| `value` | `Double` | Transaction amount (must be ≥ 0) |
| `dateTime` | `OffsetDateTime` | Timestamp of the transaction (must not be in the future) |

### `StatisticsResponseDTO`

```java
public record StatisticsResponseDTO(Long count, Double sum, Double avg, Double min, Double max) {}
```

| Field | Type | Description |
|---|---|---|
| `count` | `Long` | Number of transactions in the window |
| `sum` | `Double` | Total sum of transaction values |
| `avg` | `Double` | Average transaction value |
| `min` | `Double` | Minimum transaction value |
| `max` | `Double` | Maximum transaction value |

---

## Validation Rules

The following rules are enforced by `TransactionService` before storing a transaction:

- `value` **must not be negative** — throws `UnprocessableEntity` (HTTP 422) if `value < 0`.
- `dateTime` **must not be in the future** — throws `UnprocessableEntity` (HTTP 422) if `dateTime` is after `OffsetDateTime.now()`.

---

## Running the Application

### Prerequisites

- Java 21+ (the Docker image uses JDK 21; the build toolchain targets Java 25)
- Gradle (or use the included `./gradlew` wrapper)

### Steps

```bash
# Clone the repository
git clone <your-repo-url>
cd transaction-api

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The API will start on **`http://localhost:8080`**.

---

## Running with Docker

```bash
# 1. Build the JAR
./gradlew build

# 2. Build the Docker image
docker build -t transaction-api .

# 3. Run the container
docker run -p 8080:8080 transaction-api
```

The API will be available at **`http://localhost:8080`**.

---

## Running Tests

```bash
./gradlew test
```

Test reports are generated at:
```
build/reports/tests/test/index.html
```

### Test Coverage

| Test Class | What is tested |
|---|---|
| `TransactionServiceTest` | Add transaction, negative value exception, future date exception, clear transactions, time-range filtering |
| `TransactionControllerTest` | POST 201 success, POST 422 exception propagation, DELETE 200 success |
| `StatisticsServiceTest` | Statistics calculation with data, empty list fallback |
| `StatisticsControllerTest` | GET /statistics 200 success |

---

## Actuator & Monitoring

Spring Boot Actuator is enabled and exposes the following endpoints:

| Endpoint | URL |
|---|---|
| Health | `GET /actuator/health` |
| Info | `GET /actuator/info` |
| Metrics | `GET /actuator/metrics` |

Application metadata is configured in `application.yml`:

```yaml
info:
  app:
    name: "Transactions API"
    version: 1.0.0
    description: "API responsible for managing transactions and statistics."
```

---

## API Documentation (Swagger)

The API is documented using **SpringDoc OpenAPI**. Once the application is running, access the interactive Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

or the raw OpenAPI spec at:

```
http://localhost:8080/v3/api-docs
```

---

## Author

**Rafael A. Queiroz**  
Developed in March 2026 as a solution to the [Itaú Junior Developer Challenge](https://github.com/rafaellins-itau/desafio-itau-vaga-99-junior).
