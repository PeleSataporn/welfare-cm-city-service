FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . /app/
RUN mvn clean package

FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8787
COPY --from=build /app/target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]

#FROM maven:3.6.3-openjdk-11-slim as BUILDER
#ARG VERSION=0.0.1-SNAPSHOT
#WORKDIR /build/
#COPY pom.xml /build/
#COPY src /build/src/
#
#RUN mvn clean package
#COPY target/welfare-cm-city-${VERSION}.jar target/application.jar
#
#FROM openjdk:11.0.19-jre-slim
#WORKDIR /app/
#
## Expose the port that the Spring Boot application listens on
#EXPOSE 8787
#
#COPY --from=BUILDER /build/target/application.jar /app/
#CMD java -jar /app/application.jar


## Use a base image with Java installed
#FROM openjdk:11-jdk-slim
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the Spring Boot application JAR file into the container
#COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar app.jar
#
## Expose the port that the Spring Boot application listens on
#EXPOSE 8787
#
## Set any necessary environment variables
##ENV JAVA_OPTS=""
#
## Run the Spring Boot application when the container starts
#ENTRYPOINT ["java", "-jar", "app.jar"]

# Use a base image with Java installed
#FROM openjdk:11-jdk-slim
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the Spring Boot application JAR file into the container
#COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar app.jar
#
## Expose the port that the Spring Boot application listens on
#EXPOSE 8787
#
## Set any necessary environment variables
#ENV JAVA_OPTS=""
#
## Run the Spring Boot application when the container starts
#ENTRYPOINT ["java", "-jar", "app.jar"]


# Use official base image of Java Runtim
#FROM openjdk:11-jdk-slim
#
## Set volume point to /tmp
#VOLUME /tmp
#
## Make port 8080 available to the world outside container
#EXPOSE 8080
#
## Set application's JAR file
#ARG JAR_FILE=target/welfare-cm-city-0.0.1-SNAPSHOT.jar
#
## Add the application's JAR file to the container
#ADD ${JAR_FILE} app.jar
#
## Run the JAR file
#ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
