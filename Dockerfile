FROM maven:3.9.6-eclipse-temurin-11-alpine
WORKDIR /app
COPY . .
RUN mvn clean package -Pdev -DskipTests
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["java", "-jar", "/app/target/goplaneje-0.0.1-SNAPSHOT.jar"]
