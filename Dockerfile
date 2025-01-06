# # Use a base image with Java 11 installed
# FROM openjdk:17-jdk-alpine
#
# # Set the working directory in the container
# WORKDIR /app
#
# # Copy the JAR file into the container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # Expose the port that your application listens on
# EXPOSE 8787
#
# # Set the entrypoint command to run your application
# # ENTRYPOINT ["java", "-jar", "app.jar"]
#
# # Set the entrypoint command to run your application with the JVM option
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
# Use a base image with OpenJDK 17 installed
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Install necessary dependencies (headless fonts) to avoid issues like the X11FontManager error
RUN apk add --no-cache ttf-dejavu

# Copy the JAR file into the container
COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port that your application listens on
EXPOSE 8787

# Set the entrypoint command to run your application with the JVM option
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
