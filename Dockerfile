# Use a lightweight JDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Create app directory
WORKDIR /app

# Copy built JAR
COPY target/igym-0.0.1-SNAPSHOT.jar app.jar

# Expose port (must match your Spring Boot server.port)
EXPOSE 8081

# Run the application using environment variables for Spring profile and DB config
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
