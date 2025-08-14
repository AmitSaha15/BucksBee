# ===== Build Stage =====
# Use Maven with JDK 21 to build the app
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR without running tests
RUN mvn clean package -DskipTests


# ===== Runtime Stage =====
# Use a lightweight JRE 21 to run the app
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/bucksbee-0.0.1-SNAPSHOT.jar bucksbee-v1.jar

# Expose the application port
EXPOSE 9090

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "bucksbee-v1.jar"]
