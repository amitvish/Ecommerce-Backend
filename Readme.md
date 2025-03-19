# E-Commerce Orchestration API 

This is a Spring Boot-based REST API that:
- Loads user data from an **external API** into an **in-memory H2 database**
- Provides endpoints to **fetch users based on different criteria**
- Uses **Resilience4j** for resilient API calls
- Includes **Swagger** for API documentation
- Implements **unit tests** for better code coverage
- Follows **clean code best practices**

## Tech Stack
- Java 17
- Spring Boot
- H2 Database
- Resilience4j (Circuit Breaker & Retry)
- Swagger / OpenAPI (API Documentation)
- JUnit & JaCoCo (Testing & Code Coverage)

## 🔧 Setup Instructions
### Prerequisites
- Install Java 17
- Install Maven

### Running the Application
mvn spring-boot:run

### Running Test
mvn test
