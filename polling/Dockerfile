FROM picpay/jre-11.0.7_10-alpine-base:newrelic-5.11.0

WORKDIR /app

COPY dict/target/*.jar app.jar

EXPOSE 8080

RUN sed -i 's/${NEW_RELIC_APP_NAME}/'"${APP_NAME}"'/g' /newrelic/newrelic.yml

CMD java $@ -javaagent:/newrelic/newrelic.jar -jar app.jar