FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/crm-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml /config/application.yml
ENTRYPOINT ["java","-Dspring.config.location=/config/application.yml","-jar","/app.jar"]
