FROM maven:3.9-eclipse-temurin-17-alpine
LABEL authors="ci5644equiporojo"

EXPOSE 8080
COPY './' '.'
RUN apk --no-cache add curl
RUN mvn -Dmaven.test.skip package

ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]