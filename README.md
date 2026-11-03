# bibliotheque-microservices-k3s
# Bibliothèque universitaire : architecture microservices

## Présentation
3 microservices Jakarta EE (Catalogue, Emprunts, Notifications)
qui communiquent en REST. Un retard de restitution déclenche
automatiquement une alerte.

## Architecture
(schéma ou capture)

## Technologies
Jakarta EE 11, WildFly, MySQL, Docker, Docker Compose,
Vagrant, K3s (Kubernetes), Traefik

## Phases
1. Services individuels + Dockerfile multi-stage
2. Docker Compose (directive include) + appels inter-services
3. Infrastructure as Code (Vagrant)
4. Kubernetes : Secrets, PVC, Ingress

## Lancer le projet
docker compose up --build

## Équipe
Soukaina Elgarni, Kita, Imane
