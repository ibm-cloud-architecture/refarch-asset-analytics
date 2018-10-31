FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN mkdir /conf
ADD conf /conf
COPY target/asset-consumer-0.0.1.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
