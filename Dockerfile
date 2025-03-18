FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/demo1-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
