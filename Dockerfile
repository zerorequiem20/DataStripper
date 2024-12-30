# Step 1: Build Stage (using Maven)
# Use an official OpenJDK 17 with Oracle JDK base image and Maven installed
FROM openjdk:17.0-jdk-oracle AS build

# Set working directory in the container
WORKDIR /app

# Copy the Maven POM file and download the dependencies
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the entire project (including source code and resources) to the container
COPY . /app/

# Step 2: Compile and package the application
RUN mvn clean install -DskipTests

# Step 3: Run Stage (using a smaller JDK 17 image for the runtime)
# Use a slim OpenJDK 17 Oracle image for runtime
FROM openjdk:17.0-jdk-oracle

# Set working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/adm-data-stripper-1.0-SNAPSHOT.jar /app/adm-data-stripper.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "adm-data-stripper.jar"]
