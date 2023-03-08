FROM openjdk:17-jdk-slim

WORKDIR /app
COPY ./target/spring-boot-expenses-demo-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "spring-boot-expenses-demo-0.0.1-SNAPSHOT.jar"]