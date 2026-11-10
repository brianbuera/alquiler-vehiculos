# ETAPA 1: Compilación
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar los archivos del proyecto
COPY pom.xml .
COPY src ./src

# Compilar y generar el JAR
RUN mvn clean package -DskipTests


# ETAPA 2: Ejecución
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar el JAR generado en la primera etapa
COPY --from=build /app/target/*.jar app.jar

# Puerto de Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]