# Use Maven to build the app
FROM maven:3.8.3-openjdk-17 AS build



# Copy the pom.xml file and install dependencies
COPY pom.xml .


RUN mvn clean install -DskipTests

# Copy the source code into the image
COPY src ./src

# Package the app into a jar file
RUN mvn clean package -DskipTests


FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /target/music-library-api-0.0.1-SNAPSHOT.jar music-library-api.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "music-library-api.jar"]
