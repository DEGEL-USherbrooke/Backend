FROM openjdk:8-jdk-alpine

VOLUME /tmp

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=docker","app.jar"]