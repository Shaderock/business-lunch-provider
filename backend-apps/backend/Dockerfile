FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8081
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","/app.jar"]

