# Financial Reports Service Dockerfile
# Etapa de construção
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8095/actuator/health || exit 1

EXPOSE 8095
ENTRYPOINT ["java", "-jar", "app.jar"]

