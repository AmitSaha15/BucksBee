FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/bucksbee-0.0.1-SNAPSHOT.jar bucksbee-v1.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "bucksbee-v1.jar"]