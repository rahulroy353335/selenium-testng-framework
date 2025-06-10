# Build stage
FROM maven:3.8.8-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml testng.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

# Runtime image with proper Firefox configuration
FROM eclipse-temurin:21-jre
WORKDIR /app

# Install Firefox ESR and dependencies
RUN apt-get update && \
    apt-get install -y \
    firefox-esr \
    libgtk-3-0 \
    libdbus-glib-1-2 \
    xvfb && \
    rm -rf /var/lib/apt/lists/*

# Install specific GeckoDriver version
RUN wget https://github.com/mozilla/geckodriver/releases/download/v0.34.0/geckodriver-v0.34.0-linux64.tar.gz && \
    tar -xzf geckodriver-*.tar.gz && \
    mv geckodriver /usr/local/bin/ && \
    chmod +x /usr/local/bin/geckodriver && \
    rm geckodriver-*.tar.gz

# Copy built artifacts
COPY --from=builder /app/target/*.jar /app/app.jar
COPY --from=builder /app/target/surefire-reports /app/reports

# Set Firefox binary path explicitly
ENV FIREFOX_BIN=/usr/bin/firefox-esr
ENV MOZ_HEADLESS=1

# Entrypoint with Xvfb for headless execution
ENTRYPOINT ["sh", "-c", "Xvfb :99 -screen 0 1920x1080x24 & export DISPLAY=:99 && java -jar app.jar"]