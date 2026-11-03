# Projet 4 - Phase 2

Ce dossier regroupe les services Catalogue, Emprunts et Notifications avec Docker Compose.

Cette variante reutilise uniquement les images deja presentes sur le PC et ne telecharge aucune image.

## Demarrage

```text
docker compose config
docker compose up --build -d
docker compose ps
```

## Test de la communication

1. Creer un emprunt avec une date de retour deja depassee.
2. Lancer `POST http://localhost:8081/emprunts/api/emprunts/verifier-retards`.
3. Consulter `GET http://localhost:8082/notification-service/api/notifications`.

## Arret

```text
docker compose down
```
