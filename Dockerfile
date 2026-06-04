FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

FROM tomcat:9.0-jdk11-temurin
ENV CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom"
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/food-delivery.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
