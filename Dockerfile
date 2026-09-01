# ==============================
# Build stage
# ==============================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Debug: show generated files
RUN echo "=== CRM APPLICATION TARGET ===" && \
    ls -la /app/crm-application/target/


# ==============================
# Runtime stage
# ==============================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/crm-application/target/*.jar /app/app.jar

RUN ls -la /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
