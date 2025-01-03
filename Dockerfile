# Use the official Maven image to build the app
FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn clean install -DskipTests

# Copy the entire source code
COPY src ./src

# Package the app
RUN mvn clean package -DskipTests

# Use the openjdk image to run the app
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/music-library-api-0.0.1-SNAPSHOT.jar /app/music-library-api.jar

# Expose the port the app will run on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/music-library-api.jar"]
