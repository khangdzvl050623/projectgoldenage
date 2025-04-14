# Sử dụng base image có Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy toàn bộ mã nguồn
COPY . .

# Build file jar
RUN mvn clean package -DskipTests

# Giai đoạn chạy ứng dụng
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy jar đã build từ bước trước
COPY --from=builder /app/target/goldenage-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
