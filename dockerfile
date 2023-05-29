FROM amazoncorretto:17-alpine

RUN mkdir /app
COPY ./build/libs/zedelivery-*-SNAPSHOT.jar /app/zedelivery-api.jar

ENV MONGO_USER root
ENV MONGO_PASSWORD mongodb
ENV MONGO_HOST mongo:27017

EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app/zedelivery-api.jar"]
