FROM ubuntu:latest AS build

WORKDIR /app

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

COPY . /app

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/todolist-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]