FROM openjdk:17-jdk-alpine
VOLUME /tmp
EXPOSE 8787
COPY ./target/welfare-cm-city-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]