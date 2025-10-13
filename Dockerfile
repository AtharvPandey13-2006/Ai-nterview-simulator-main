# ---- Stage 1: Build the app ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the app ----
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy only the final JAR from the builder stage
COPY --from=builder /app/target/*SNAPSHOT.jar app.jar

# Expose the dynamic port (Render sets $PORT)
EXPOSE 8080

# Start Spring Boot app on Render's assigned port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]
