FROM maven:3.8.6-jdk-11-slim as build
COPY src ./src
COPY pom.xml ./
RUN mvn -f ./pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build ./target/string_randomizer-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]
EXPOSE 8080