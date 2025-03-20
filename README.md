# **DeviceAtlas Backend**

## **Overview**

DeviceAtlas Backend is a Spring Boot application designed to process User-Agent strings and store device information. It utilizes local caching, Docker for containerization, and Swagger for API documentation.

## **Features**

- **User-Agent Processing**: Parses and stores device information.
- **Local Caching**: Reduces redundant API calls and improves performance.
- **Concurrency Control**: Uses locking mechanisms to prevent race conditions.
- **Duplicate Detection**: Ensures that only unique device data is stored.
- **Data Filtering**: Filters out non-tablets User-Agent.
- **Swagger API Documentation**: Provides an interactive UI for testing endpoints.
- **Docker Support**: Easily containerize and deploy the application.
- **Test Coverage**: Ensures high code reliability through unit tests.

## **Getting Started**

### **Prerequisites**

Ensure you have the following installed:

- Java 17+
- Gradle 8+
- Docker (optional for containerization)

### **Clone the Repository**

```
https://github.com/xkong-study/DeviceAtlas_backEnd.git
```

### **Run Locally**

```
./gradlew build
./gradlew run
```

Application runs on <http://localhost:8080>.

## **API Documentation (Swagger)**

Swagger UI is available at:

<http://localhost:8080/swagger-ui/index.html#/user-agent-controller/fetchDevices>.

## **Local Caching**

The application uses an in-memory cache to store previously parsed User-Agent data.

- **Cache Type**: HashMap
- **Can Optimize by using Eviction Policy**: LRU (Least Recently Used)

## **Concurrency Control, Deduplication, and Filtering**

- **Locking Mechanism**:  Uses in-memory caching to prevent redundant processing and improve efficiency.
- **Duplicate Detection**: Checks if a User-Agent string is already processed before storing it.
- **Filtering**: Skips non-tablets User-Agent.

## **Running with Docker**

### **Run Docker Container**

```
docker compose build
docker compose up -d
```

## **Running Tests & Coverage Report**

### **Run Unit Tests**

```
./gradlew test
```

### **Generate Code Coverage Report**

```
./gradlew test jacocoTestReport 
open build/reports/jacoco/test/html/index.html
```
