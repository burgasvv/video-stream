FROM maven:latest AS build
COPY pom.xml .
COPY /src ./src/
RUN mvn clean package -DskipTests

FROM openjdk:24 AS prod
LABEL authors="Burgas"

COPY --from=build target/database-server-0.0.1-SNAPSHOT.jar database-server.jar
EXPOSE 8888

ENTRYPOINT ["java", "-jar", "database-server.jar"]