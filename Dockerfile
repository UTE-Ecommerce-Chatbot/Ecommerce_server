FROM openjdk:8-jdk-alpine

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk

WORKDIR /app

COPY target/boec-0.0.1-SNAPSHOT.jar /app/myapp.jar


EXPOSE 8087

ENTRYPOINT ["java", "-jar", "myapp.jar"]
