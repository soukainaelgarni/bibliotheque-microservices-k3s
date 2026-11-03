# Service Emprunts - Phase 2

Ce service detecte les emprunts dont la date de retour est depassee et appelle le service Notifications.

## URL de Notifications

La variable Docker suivante est configuree dans `compose.yaml` :

```text
NOTIFICATION_URL=http://notification-service:8080/notification-service/api/notifications
```

## Verification des retards

La verification est executee automatiquement toutes les minutes. Elle peut aussi etre lancee immediatement :

```http
POST http://localhost:8081/emprunts/api/emprunts/verifier-retards
```

## Tests principaux

```http
POST http://localhost:8081/emprunts/api/emprunts
GET  http://localhost:8081/emprunts/api/emprunts
GET  http://localhost:8081/emprunts/api/emprunts/retards
```

Exemple de creation d'un emprunt deja depasse :

```json
{
  "etudiantId": 1,
  "documentId": 1,
  "dateRetourPrevue": "2026-06-20"
}
```

Le service Notifications doit etre lance sur le meme reseau Docker avant de verifier les retards.
