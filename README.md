# **DeviceAtlas Backend**

## **Overview**

DeviceAtlas Backend is a Spring Boot application designed to process User-Agent strings and store device information. It utilizes local caching, Docker for containerization, and Swagger for API documentation.

## **Features**

**User-Agent Processing**: Parses and stores device information.       
**Local Caching**: Reduces redundant API calls and improves performance.       
**Concurrency Control**: Uses locking mechanisms to prevent race conditions.        
**Duplicate Detection**: Ensures that only unique device data is stored.      
**Data Filtering**: Filters out non-tablets User-Agent.           
**Swagger API Documentation**: Provides an interactive UI for testing endpoints.             
**Docker Support**: Easily containerize and deploy the application.        
**Test Coverage**: Ensures high code reliability through unit tests.     

## **Getting Started**

### **Prerequisites**

Ensure you have the following installed:

- Java 17+
- Gradle 8+
- Docker (optional for containerization)
- postgres:14 (username:postgres, password:deviceAtlas)

### **Clone the Repository**

```
https://github.com/xkong-study/DeviceAtlas_backEnd.git
```

### **Run Locally**

open postgres    
```
sudo systemctl start postgresql  # Linux
brew services start postgresql   # macOS

```
run springboot   
```
./gradlew build
./gradlew run
```

Application runs on <http://localhost:8080>.

## **API Documentation (Swagger)**

Swagger UI is available at:

<http://localhost:8080/swagger-ui/index.html#/user-agent-controller/fetchDevices>.

<img width="800" alt="Screenshot 2025-03-20 at 00 51 59" src="https://github.com/user-attachments/assets/2a745d82-d905-434d-82ad-d76ee48765da" />
<img width="800" alt="Screenshot 2025-03-20 at 00 52 20" src="https://github.com/user-attachments/assets/429a682d-aae3-4a3e-80af-ccde19476642" />


## **Local Caching**

The application uses an in-memory cache to store previously parsed User-Agent data.

- **Cache Type**: HashMap
- **Can Optimize by using Eviction Policy**: LRU (Least Recently Used)

<img width="683" alt="Screenshot 2025-03-20 at 00 52 58" src="https://github.com/user-attachments/assets/a1a93a59-87e6-4fe1-97e0-b9f9e8892879" />

## **Concurrency Control, Deduplication, and Filtering**

- **Locking Mechanism**:  Uses in-memory caching to prevent redundant processing and improve efficiency.

<img width="612" alt="Screenshot 2025-03-20 at 00 55 06" src="https://github.com/user-attachments/assets/d0d9aaa0-ee88-4161-8a3f-7155c6c0999a" />

- **Duplicate Detection**: Checks if a User-Agent string is already processed before storing it.

```
Optional<DeviceInfo> existingDevice = deviceRepository.findByModelAndOsVersionAndVendorAndBrowserNameAndOsNameAndPrimaryHardwareTypeAndBrowserRenderingEngine(
                        model, osVersion, vendor, browserName, osName, hardwareType, browserRenderingEngine
                );
```
- **Filtering**: Skips non-tablets User-Agent.
  
<img width="600" alt="Screenshot 2025-03-20 at 00 53 52" src="https://github.com/user-attachments/assets/4607178e-487f-47f5-87e2-23b2a7b148cb" />

## **Running with Docker**

### **Run Docker Container**

```
docker compose build
docker compose up -d
```
<img width="643" alt="Screenshot 2025-03-20 at 00 56 18" src="https://github.com/user-attachments/assets/0c75c611-b089-4fe0-ae70-a95e5ca992fc" />


## **Running Tests & Coverage Report**

### **Run Unit Tests**

```
./gradlew test
```
<img width="599" alt="Screenshot 2025-03-20 at 00 57 06" src="https://github.com/user-attachments/assets/47fe5db5-18e9-4795-a0f7-0b417d2215be" />


### **Generate Code Coverage Report**

```
./gradlew test jacocoTestReport 
open build/reports/jacoco/test/html/index.html
```
<img width="559" alt="Screenshot 2025-03-20 at 00 57 30" src="https://github.com/user-attachments/assets/2d9bde29-e5ce-4d09-8267-f226811a8ddf" />


