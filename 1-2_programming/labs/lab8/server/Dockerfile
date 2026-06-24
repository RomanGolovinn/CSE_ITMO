FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY build/libs/server.jar app.jar

EXPOSE 1234

ENTRYPOINT ["java", "-jar", "app.jar"]