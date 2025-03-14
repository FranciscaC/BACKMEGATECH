# Etapa 1: Construcción del proyecto con Maven
FROM maven:3.9.2-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiar el pom.xml y descargar las dependencias (optimización de cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el resto del código fuente y compilar el proyecto
COPY src ./src
RUN mvn clean package -DskipTests -Pproduction

# Etapa 2: Imagen de Producción ligera con OpenJDK 21
FROM openjdk:17-alpine
WORKDIR /app

# Copiar el jar generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto en el que la aplicación escuchará
EXPOSE 8080

# Nota: Las variables de entorno se inyectan en tiempo de ejecución en Railway
ENTRYPOINT ["java", "-jar", "app.jar"]
