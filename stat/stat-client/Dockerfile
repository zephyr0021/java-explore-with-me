FROM amazoncorretto:21-alpine
LABEL authors="kirillfedorov"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]