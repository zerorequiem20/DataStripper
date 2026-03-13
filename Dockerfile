# ===== Runtime Dockerfile for your app =====
# Use Eclipse Temurin Java 17 JRE (runtime only)
FROM eclipse-temurin:17-jre

# Set working directory inside container
WORKDIR /app

# Copy the JAR built by Jenkins into the container
COPY adm-data-stripper-1.0-SNAPSHOT.jar adm-data-stripper.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]