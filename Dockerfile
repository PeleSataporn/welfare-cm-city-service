FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8787
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
