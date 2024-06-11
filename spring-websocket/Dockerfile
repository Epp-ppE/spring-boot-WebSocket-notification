# Dependencies
FROM ghcr.io/cirruslabs/flutter:3.22.2 as packager
WORKDIR /home/app
COPY build.gradle .
RUN gradle dependencies

# Build
FROM packager as builder
COPY . .
RUN gradle build -x test
RUN rm build/libs/*-plain.jar

# Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /home/app
COPY --from=builder /home/app/build/libs/*.jar .
ENV SPRING_OUTPUT_ANSI_ENABLED=always

EXPOSE 8080
ENTRYPOINT java -jar ./*.jar