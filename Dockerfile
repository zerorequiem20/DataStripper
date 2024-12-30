# Step 1: Build Stage (using OpenJDK and Maven)
# Use OpenJDK 17 Oracle base image for build stage
FROM openjdk:17.0-jdk-oracle AS build

# Install Maven (since it's not included in the OpenJDK image)
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Set working directory in the container
WORKDIR /app

# Copy the Maven POM file and download the dependencies
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the entire project (including source code and resources) to the container
COPY . /app/

# Step 2: Compile and package the application
RUN mvn clean install -DskipTests

# Step 3: Run Stage (using a smaller JDK 17 image for runtime)
# Use OpenJDK 17 Oracle base image for runtime
FROM openjdk:17.0-jdk-oracle

# Set working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/adm-data-stripper-1.0-SNAPSHOT.jar /app/adm-data-stripper.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]
