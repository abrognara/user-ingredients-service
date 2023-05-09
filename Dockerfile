FROM amazoncorretto:17-alpine-jdk
EXPOSE 8080
ADD target/user-ingredients-service-0.0.1-SNAPSHOT.jar user-ingredients-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local-dynamo", "/user-ingredients-service.jar"]