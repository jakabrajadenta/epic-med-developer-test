# Epic Medical Developer Test

---

**Study Case: Paginated API with External Data Source**

## 1. Project Overview

This project was developed as part of a **developer technical test** for Epic Med.
The main goal is to demonstrate the implementation of a **paginated REST API** 
that retrieves and transforms data from an **external API source**.

The project focuses on:

* Clean and simple API design
* Pagination handling
* External API consumption
* Data mapping and response formatting
* Readable and maintainable code

This is intentionally built as a **lightweight and minimal project**, 
without complex architecture layers, to emphasize clarity and correctness.

---

## 2. Project Scope

### In Scope

* Expose **one REST API endpoint**
* Consume data from an **external public API**
* Support **pagination**
* Transform external response into a custom API response format
* Provide clear configuration and setup
* Simple caching

### Out of Scope

* Authentication & authorization
* Database persistence
* Complex architecture patterns (CQRS, Hexagonal, etc.)
* Hard caching or message queues

---

## 3. Tech Stack

| Technology                   | Description                         |
|------------------------------| ----------------------------------- |
| **Java 17**                  | Programming language                |
| **Spring Boot 3++**          | Application framework               |
| **Spring Web**               | REST API development                |
| **RestTemplate / WebClient** | HTTP client to consume external API |
| **Maven**                    | Build & dependency management       |
| **Jackson**                  | JSON serialization/deserialization  |
| **Lombok**                   | Reduce boilerplate code             |
| **JUnit**                    | Unit testing framework              |

---

## 4. Setup & Installation Guide

### Prerequisites

Make sure you have the following installed:

* **Java 17+**
* **Maven 3.8+**
* **Git**

### Clone Repository

```bash
git clone https://github.com/jakabrajadenta/epic-med-developer-test.git
cd epic-med-developer-test
```

### Build Project

```bash
mvn clean install
```

---

## 5. How to Run the Application

### Run Using Maven

```bash
mvn spring-boot:run
```

### Run Using JAR

```bash
java -jar target/epic-med-developer-test-*.jar
```

### Application Default URL

```
http://localhost:8080
```

---

## 6. Configuration Explanation

### application.properties / application.yml

This file is used to configure:

* Server port
* External API base URL
* Timeout and HTTP client settings

Example:

```properties
server.port=8080
external.api.base-url=https://dummyjson.com
```

### Environment Variables (Optional)

```bash
EXTERNAL_API_BASE_URL=https://dummyjson.com
```

---

## 7. Maven (`pom.xml`) Dependency Explanation

Key dependencies and their purpose:

| Dependency                 | Purpose                                             |
| -------------------------- | --------------------------------------------------- |
| `spring-boot-starter-web`  | Build REST APIs                                     |
| `spring-boot-starter-test` | Unit & integration testing                          |
| `lombok`                   | Reduce boilerplate (getters, setters, constructors) |
| `jackson-databind`         | JSON serialization/deserialization                  |

Example snippet:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## 8. Folder Structure Explanation

```text
src/main/java
└── com.epicmed.developer.assessment
    ├── cache
    ├── client
    ├── config
    ├── controller
    │   └── advice
    ├── dto
    │   └── error
    ├── exception
    ├── filter
    ├── model
    ├── service
    ├── util
    │   └── constant
    └── Application.java
```

### Folder Responsibility

* **config**
  Configuration all class and bean
* **controller**
  Exposes REST API endpoints
* **service**
  Business logic & pagination handling
* **client**
  External API communication
* **dto**
  Request & response data mapping
* **model**
  Entity data or real data model
* **cache**
  Cache manager to keep user data
* **exception**
  Exception builder to costumize error
* **filter**
  Filter class as handler or interceptor request
* **util**
  Utilisation class for general usage

* **Application**
  Spring Boot entry point

---

## 9. API Documentation

### Endpoint: Get Users (Paginated)

#### URL

```
GET /api/users
```

#### Query Parameters

| Parameter | Type    | Required | Description              |
|-----------|---------|----------|--------------------------|
| `page`    | Integer | Yes      | Page number (default: 1) |
| `size`    | Integer | Yes      | Page size (default: 10)  |
| `name`    | String  | Optional | Filter data by name      |
| `email`   | String  | Optional | Filter data by email     |

---

### Sample Request

```bash
curl -X GET "http://localhost:8080/api/users?page=1&size=5"
```

---

### Sample Response

```json
{
  "page": 1,
  "size": 5,
  "totalPages": 2,
  "totalElements": 10,
  "data": [
    {
      "id": 1,
      "email": "user1@example.com",
      "firstName": "John",
      "lastName": "Doe",
      "avatar": "https://example.com/avatar/1.png"
    },
    {
      "id": 2,
      "email": "user2@example.com",
      "firstName": "Jane",
      "lastName": "Smith",
      "avatar": "https://example.com/avatar/2.png"
    }
  ]
}
```

---

## 10. External API Source

This project consumes data from:

```
https://dummyjson.com/users
```

The external API response is transformed into a **custom response structure** to fit the project requirements.

---

## 11. Notes

* This project is intentionally **simple and focused**
* Designed for **evaluation and readability**
* Easily extendable for:

    * Authentication
    * Database persistence
    * Message queue
    * Multiple endpoints

---

## 12. Author

**Jaka Brajadenta**
GitHub: [https://github.com/jakabrajadenta](https://github.com/jakabrajadenta)

---
