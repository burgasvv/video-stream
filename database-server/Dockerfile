FROM maven:latest AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package

FROM openjdk:latest AS prod
LABEL authors="Burgas"

COPY --from=build target/database-server-0.0.1-SNAPSHOT.jar database-server.jar
EXPOSE 8888

ENTRYPOINT ["java", "-jar", "database-server.jar"]