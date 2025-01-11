# # Use a base image with Java 11 installed
# FROM openjdk:17-jdk-alpine
#
# # Set the working directory in the container
# WORKDIR /app
#
# # Install necessary dependencies (headless fonts) to avoid issues like the X11FontManager error
# # RUN apk add --no-cache ttf-dejavu
# # ตั้งค่าตัวแปร Locale
# ENV LC_ALL=C.UTF-8
# ENV LANG=C.UTF-8
# ENV LANGUAGE=C.UTF-8
#
# # ติดตั้ง dependencies
# RUN apk add --no-cache curl
#
# # Copy the JAR file into the container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # Expose the port that your application listens on
# EXPOSE 8787
#
# # Set the entrypoint command to run your application
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Set the entrypoint command to run your application with the JVM option
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]

# FROM openjdk:17-jdk-alpine
#
# # Set the working directory in the container
# WORKDIR /app
#
# # Set environment variables for locale
# ENV LC_ALL=C.UTF-8
# ENV LANG=C.UTF-8
# ENV LANGUAGE=C.UTF-8
#
# # Install necessary dependencies
# # RUN apk add --no-cache freetype ttf-dejavu
# RUN apk add --no-cache ttf-dejavu ttf-th-sarabun
#
# # Copy the JAR file into the container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # Expose the port that your application listens on
# EXPOSE 8787
#
# # Set the entrypoint command to run your application with headless mode
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]

# # ใช้ base image ที่มี Java
# FROM openjdk:17-jdk-alpine
#
# # ตั้งค่า work directory
# WORKDIR /app
#
# # คัดลอกฟอนต์ TH Sarabun PSK (ฟอนต์ .ttf) ไปยัง container
# COPY ./resources/fonts/THSarabun.ttf /usr/share/fonts/
#
# # อัพเดท cache ของฟอนต์
# RUN fc-cache -f -v
#
# # ติดตั้ง dependencies อื่น ๆ
# RUN apk add --no-cache ttf-dejavu
#
# # คัดลอก JAR ไปยัง container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # เปิด port
# EXPOSE 8787
#
# # ตั้งค่า entrypoint
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]

# FROM openjdk:17-jdk-alpine
#
# # ตั้งค่า working directory
# WORKDIR /app
#
# # ติดตั้ง dependencies รวมถึง fontconfig สำหรับการใช้ fc-cache
# RUN apk add --no-cache ttf-dejavu fontconfig
#
# # คัดลอกฟอนต์จาก resources/fonts ไปยัง container
# COPY repo/main/resources/fonts/THSarabun.ttf /usr/share/fonts/
#
# # อัพเดท cache ของฟอนต์
# RUN fc-cache -f -v
#
# # คัดลอก JAR ไปยัง container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # เปิด port
# EXPOSE 8787
#
# # ตั้งค่า entrypoint
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]

# FROM openjdk:17-jdk-alpine
#
# # ตั้งค่า working directory
# WORKDIR /app
#
# # ติดตั้ง dependencies รวมถึง fontconfig สำหรับการใช้ fc-cache
# RUN apk add --no-cache ttf-dejavu fontconfig
#
# # คัดลอก JAR ไปยัง container
# COPY repo/sarabun-report.jar /app/sarabun-report.jar
#
# # ดึงฟอนต์จาก JAR และคัดลอกฟอนต์ทั้งหมดจาก path fonts/SARABUN/
# RUN mkdir -p /usr/share/fonts/ && \
#     jar xf /app/sarabun-report.jar fonts/SARABUN/ && \
#     mv fonts/SARABUN/* /usr/share/fonts/
#
# # อัพเดท cache ของฟอนต์
# RUN fc-cache -f -v
#
# # คัดลอก JAR ของแอปพลิเคชันไปยัง container
# COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar
#
# # เปิด port
# EXPOSE 8787
#
# # ตั้งค่า entrypoint
# ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]

#####################################################################
FROM openjdk:17-jdk-alpine

# ตั้งค่า working directory
WORKDIR /app

# ติดตั้ง dependencies รวมถึง fontconfig สำหรับการใช้ fc-cache
RUN apk add --no-cache ttf-dejavu fontconfig

# # คัดลอก JAR ไปยัง container
# COPY repo/sarabun-report.jar /app/sarabun-report.jar

# # ดึงฟอนต์จาก JAR และคัดลอกฟอนต์ทั้งหมดจาก path fonts/SARABUN/
# RUN mkdir -p /usr/share/fonts/ && \
#     jar xf /app/sarabun-report.jar fonts/SARABUN/ && \
#     mv fonts/SARABUN/* /usr/share/fonts/ && \
#     fc-cache -f -v

# คัดลอกฟอนต์จาก resources/fonts ไปยัง container
COPY src/main/resources/fonts/SARABUN /usr/share/fonts/

# อัพเดท cache ของฟอนต์
RUN fc-cache -f -v

# คัดลอก JAR ของแอปพลิเคชันไปยัง container
COPY target/welfare-cm-city-0.0.1-SNAPSHOT.jar /app/app.jar

# เปิด port
EXPOSE 8787

# ตั้งค่า entrypoint
ENTRYPOINT ["java", "-Djava.awt.headless=true", "-jar", "app.jar"]
