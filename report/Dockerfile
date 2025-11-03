# Multi-stage build
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy parent pom first
COPY ../pom.xml ../pom.xml

# Copy project pom and source
COPY pom.xml .
COPY src ./src

# Build the application (skip tests for faster build)
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set environment variables (can be overridden by docker-compose)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/animaldb
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=admin123
ENV SPRING_ACTIVEMQ_BROKER_URL=tcp://activemq:61616
ENV SPRING_ACTIVEMQ_USER=admin
ENV SPRING_ACTIVEMQ_PASSWORD=admin

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
