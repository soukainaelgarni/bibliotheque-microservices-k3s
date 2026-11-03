# ═══ Stage 1 : BUILD avec Maven ═══
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copier tout le projet
COPY src ./src
COPY web ./web
COPY pom.xml .

# Compiler et créer le WAR avec Maven
RUN mvn clean package -DskipTests

# ═══ Stage 2 : RUN avec WildFly ═══
FROM quay.io/wildfly/wildfly:38.0.0.Final-jdk21

# Variables de connexion MySQL
ENV DB_HOST=mysql-emprunts \
    DB_PORT=3306 \
    DB_NAME=emprunts_db \
    DB_USER=emprunts_user \
    DB_PASSWORD=emprunts_pass

# Copier le module MySQL dans WildFly
COPY mysql/ \
    /opt/jboss/wildfly/modules/com/mysql/

# Copier la configuration WildFly
COPY standalone-full.xml \
    /opt/jboss/wildfly/standalone/configuration/standalone-full.xml

# Copier le WAR compilé depuis le stage Build
COPY --from=build /app/target/emprunts.war \
    /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080

CMD ["/opt/jboss/wildfly/bin/standalone.sh", \
     "-b", "0.0.0.0", \
     "-c", "standalone-full.xml"]