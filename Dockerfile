# Step 1: Build Stage (using Maven)
# Use an official Maven image with OpenJDK 17 (Oracle version)
FROM maven:3.8.6-openjdk-17-oracle AS build  # Use the correct Maven image with OpenJDK 17 Oracle

# Set working directory in the container
WORKDIR /app

# Copy the Maven POM file and download the dependencies
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the entire project (including source code and resources) to the container
COPY . /app/

# Step 2: Compile and package the application
RUN mvn clean install -DskipTests

# Step 3: Run Stage (using Oracle JDK 17 image for the runtime)
FROM openjdk:17-oracle  # Use Oracle JDK 17 for the runtime image

# Set working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/adm-data-stripper-1.0-SNAPSHOT.jar /app/adm-data-stripper.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]
