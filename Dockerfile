# Start from Maven image to build the project
FROM maven:3.9.3-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy only the pom.xml first to leverage Docker cache for dependencies
COPY pom.xml .

# Download dependencies (will cache unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the Spring Boot jar
RUN mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose default Spring Boot port
EXPOSE 8005

# Pass environment variables to Spring Boot (example: email config)
# These can also be set directly in Render as environment variables
ENV JAVA_OPTS=""

# Command to run the jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]