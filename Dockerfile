# Use a base Java image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/goldenage-0.0.1-SNAPSHOT.jar app.jar

# Expose port (change if your app runs on a different port)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
