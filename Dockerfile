# First stage: Build the application using Maven
FROM maven:3.8.3-openjdk-17 AS build

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml and the src folder to the working directory
COPY . .

# Run the maven build to package the Spring Boot application
RUN mvn clean package -DskipTests]1

# Second stage: Run the application using OpenJDK
FROM openjdk:17.0.1-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/MusicLibrary-0.0.1-SNAPSHOT.jar MusicLibrary.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "MusicLibrary.jar"]
