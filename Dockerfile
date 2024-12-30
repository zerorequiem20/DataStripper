# Step 1: Build Stage (using OpenJDK and Maven)
# Use OpenJDK 17 Slim image for build stage
FROM openjdk:17-jdk-slim AS build

# Install Git and Maven (since they are not included in the OpenJDK image)
RUN apt-get update && \
    apt-get install -y git maven && \
    apt-get clean

# Set working directory in the container
WORKDIR /app

# Clone the repository from GitHub
# Replace <username>/<repo> with your actual GitHub username and repository name
RUN git clone https://github.com/zerorequiem20/DataStripper .

# Download Maven dependencies
RUN mvn dependency:go-offline

# Step 2: Compile and package the application
RUN mvn clean install -DskipTests

# Verify the JAR file creation in the target directory
RUN ls -la /app/target

# Step 3: Run Stage (using a smaller JDK 17 image for runtime)
# Use OpenJDK 17 Slim base image for runtime
FROM openjdk:17-jdk-slim

# Set working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/adm-data-stripper-1.0-SNAPSHOT.jar /app/adm-data-stripper.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]
