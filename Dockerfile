FROM openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 8787
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]