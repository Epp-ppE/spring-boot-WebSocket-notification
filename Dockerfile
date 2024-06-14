# Use the Cirrus Labs Flutter image for building
FROM ghcr.io/cirruslabs/flutter:3.22.2 AS flutter-builder

# Set the working directory
WORKDIR /app

# Copy the pubspec.yaml and pubspec.lock files to leverage Docker cache
COPY flutter_web_socket/pubspec.* ./

# Install dependencies
RUN flutter pub get

# Copy the entire project
COPY flutter_web_socket/lib ./lib
COPY flutter_web_socket/web ./web

# Build the Flutter application
RUN flutter build web

# Dependencies
FROM gradle:jdk21 as spring-builder
WORKDIR /home/app
COPY spring-websocket/build.gradle .
RUN gradle dependencies
COPY spring-websocket/src ./src
COPY --from=flutter-builder /app/build/web src/main/resources/static
RUN gradle build -x test
RUN rm build/libs/*-plain.jar

# Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /home/app
COPY --from=spring-builder /home/app/build/libs/*.jar .
ENV SPRING_OUTPUT_ANSI_ENABLED=always

EXPOSE 8080
ENTRYPOINT java -jar ./*.jar