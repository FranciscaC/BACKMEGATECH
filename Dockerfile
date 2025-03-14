# Step 1: Build the project with Maven
FROM maven:3.9.2-eclipse-temurin:21 AS builder
WORKDIR /app

# Copy the pom.xml file and download dependencies (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code and compile the project
COPY src ./src
RUN mvn clean package -DskipTests -Pproduction

# Stage 2: Light Production Image with JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the jar file generated in the previous step
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application will listen on (adjust according to your configuration)
EXPOSE 8080

# Run the application with Java
ENTRYPOINT ["java", "-jar", "app.jar"]
