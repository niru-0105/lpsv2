# Stage 1: Build the application
FROM maven:3.8.7-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download the dependencies
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file from the builder stage
COPY --from=builder /app/target/loan-processing-system-0.0.1-SNAPSHOT.jar .

# Expose the port the application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "loan-processing-system-0.0.1-SNAPSHOT.jar"]


#docker run -p 8080:8080 \
#    -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/testdb \
#    -e SPRING_DATASOURCE_USERNAME=root \
#    -e SPRING_DATASOURCE_PASSWORD=12345678 \
#    my-spring-boot-app

