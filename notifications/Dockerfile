# build maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY web ./web
RUN mvn clean package -DskipTests

# wildfly runtime
FROM quay.io/wildfly/wildfly:30.0.1.Final-jdk17
COPY --from=build /app/target/notification-service.war /opt/jboss/wildfly/standalone/deployments/
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]