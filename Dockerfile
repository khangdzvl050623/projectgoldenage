# Build stage
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/goldenage-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8383
ENTRYPOINT ["java", "-jar", "app.jar"]
