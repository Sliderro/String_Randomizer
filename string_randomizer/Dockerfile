FROM maven:3.8.1-jdk-11-slim as build
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY src ./src
COPY pom.xml ./
RUN mvn -f ./pom.xml clean package
#Todo: Caching

FROM openjdk:11-jre-slim
WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /opt/app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]