# Use a base image with Java 11 installed
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your application listens on
EXPOSE 8787

# Set the entrypoint command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]