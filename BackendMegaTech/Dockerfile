# Use a base image of OpenJDK (Java 21)
FROM openjdk:21-jdk-slim

# Maintainer information (optional)
LABEL maintainer="gersontajiboy8@gmail.com"

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the 'target' directory to the container
COPY target/BackendMegaTech-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will use (default is 8080 in Spring Boot)
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
