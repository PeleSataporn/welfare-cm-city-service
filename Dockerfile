FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8787
COPY /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
