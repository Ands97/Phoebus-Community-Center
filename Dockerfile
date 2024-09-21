FROM maven:3.8.3-openjdk-17 AS builder

COPY pom.xml /app/

COPY src /app/src/

WORKDIR /app

RUN mvn clean package -DskipTests

FROM openjdk:17-alpine

COPY --from=builder /app/target/community-center-0.0.1-SNAPSHOT.jar /app/community-center-api.jar

ENTRYPOINT ["java", "-jar", "/app/community-center-api.jar"]
