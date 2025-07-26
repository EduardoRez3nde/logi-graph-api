FROM gradle:8.8-jdk-21-and-22 AS builder
WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]