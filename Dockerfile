
FROM adoptopenjdk/openjdk11:jre-11.0.4_11-alpine

WORKDIR /app

COPY application/target/*.jar app.jar

EXPOSE 8080

CMD java -jar app.jar

