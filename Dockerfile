# Use Maven to build the app
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/music-library-api-0.0.1-SNAPSHOT.jar music-library-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "music-library-api.jar"]