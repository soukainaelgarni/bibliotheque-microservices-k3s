# Construire le WAR avec un JDK officiel.
FROM eclipse-temurin:17-jdk AS build
USER root
WORKDIR /build

COPY src ./src
COPY web ./web
COPY docker-libs/jakarta.jakartaee-api-10.0.0.jar /tmp/jakarta-api.jar

RUN mkdir -p classes war/WEB-INF/classes && \
    javac -encoding UTF-8 -source 17 -target 17 \
      -cp /tmp/jakarta-api.jar \
      -d classes $(find src/java -name "*.java") && \
    cp -r web/. war/ && \
    cp -r classes/. war/WEB-INF/classes/ && \
    mkdir -p war/WEB-INF/classes/META-INF && \
    cp src/META-INF/persistence.xml war/WEB-INF/classes/META-INF/ && \
    cd war && jar -cf /build/bibliotheque-catalogue-projet.war .

# Utiliser une vraie image WildFly officielle (independante des autres services).
FROM quay.io/wildfly/wildfly:38.0.0.Final-jdk21
USER root

COPY --from=build /build/bibliotheque-catalogue-projet.war /opt/jboss/wildfly/standalone/deployments/
COPY docker-libs/mysql-connector-j-8.4.0.jar /opt/jboss/wildfly/standalone/deployments/
COPY docker-libs/mysql-catalogue-ds.xml /opt/jboss/wildfly/standalone/deployments/

USER jboss
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]