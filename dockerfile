# Base image with Java + Maven
FROM maven:3.8.8-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built JAR and test resources
COPY --from=builder /app/target/*.jar /app/app.jar
COPY --from=builder /app/target/surefire-reports /app/reports

# Install Firefox and GeckoDriver
RUN apt-get update && \
    apt-get install -y firefox-esr && \
    wget https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz && \
    tar -xzf geckodriver-*.tar.gz && \
    mv geckodriver /usr/local/bin/ && \
    rm geckodriver-*.tar.gz

ENTRYPOINT ["java", "-jar", "app.jar"]