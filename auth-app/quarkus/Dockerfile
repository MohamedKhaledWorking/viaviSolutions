FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -DskipTests dependency:go-offline

COPY . .
RUN mvn -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /work
COPY --from=build /app/target/quarkus-app/ /work/quarkus-app/
EXPOSE 8080
CMD ["java","-Dquarkus.http.host=0.0.0.0","-jar","/work/quarkus-app/quarkus-run.jar"]