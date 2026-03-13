# ===== Runtime-only Dockerfile for your app =====
# Use OpenJDK 17 slim for a small runtime image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the JAR built by Jenkins into the container
COPY adm-data-stripper-1.0-SNAPSHOT.jar adm-data-stripper.jar

# Expose port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]