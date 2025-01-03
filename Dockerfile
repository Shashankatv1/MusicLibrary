# First stage: Build the application using Maven
FROM maven:3.8.3-openjdk-17 AS build

COPY . .

RUN mvn clean package -DskipTests


FROM openjdk:17.0.1-jdk-slim

COPY --from=build /target/MusicLibrary-0.0.1-SNAPSHOT.jar MusicLibrary.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "MusicLibrary.jar"]
