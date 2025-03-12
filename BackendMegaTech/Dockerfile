# Step 1: Building the project with Maven
FROM maven:3.9.2-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy the pom.xml file and download the dependencies (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and compile the project
COPY src ./src
RUN mvn clean package -DskipTests -Pproduction

# Stage 2: Light Production Image with JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the jar generated in the previous step
COPY --from=builder /app/target/*.jar app.jar

# Exposes the port the application will listen on (adjust according to your configuration)
EXPOSE 8080

# Note: Environment variables are injected at runtime in Railway
ENTRYPOINT ["java", "-jar", "app.jar"]
