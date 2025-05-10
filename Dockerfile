# Build stage
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline

# Build application
COPY src ./src
RUN mvn clean package -DskipTests && \
    mkdir -p target/dependency && \
    (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM eclipse-temurin:17.0.4.1_1-jre-jammy

# Add non-root user for security
RUN useradd -m appuser && \
    mkdir -p /app && \
    chown appuser:appuser /app
USER appuser
WORKDIR /app

# Copy dependencies and application
COPY --from=build --chown=appuser:appuser /app/target/dependency/BOOT-INF/lib ./lib
COPY --from=build --chown=appuser:appuser /app/target/dependency/META-INF ./META-INF
COPY --from=build --chown=appuser:appuser /app/target/dependency/BOOT-INF/classes ./

# Runtime configuration
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
ENTRYPOINT ["java", "-cp", ".:lib/*", "com.shashank.musiclibrary.MusicLibraryApplication"]