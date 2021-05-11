FROM openjdk:11-jdk-slim AS build-env

ADD . /app
WORKDIR /app

RUN ./mvnw package

FROM gcr.io/distroless/java:11

COPY --from=build-env /app/target/creditservice.jar /app/creditservice.jar
WORKDIR /app

EXPOSE 8081

CMD ["creditservice.jar"]