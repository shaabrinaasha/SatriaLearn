# Stage 1: Build with Gradle
FROM gradle:8.5-jdk-alpine AS builder

# Copy only the build.gradle file first to leverage Docker cache
COPY build.gradle .
# Copy the entire source code
COPY src ./src

# Run Gradle build
RUN gradle clean build

# Stage 2: Run with Java 17
FROM openjdk:17-alpine

# Copy the built JAR from the previous stage
ARG JAR_FILE=build/libs/properly-0.0.1-SNAPSHOT.jar
COPY --from=builder ${JAR_FILE} app.jar

# Expose port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
