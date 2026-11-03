# Projet 4 - Phase 4 : Orchestration et Deploiement (Kubernetes / K3s)

Cette phase deploie les services Catalogue, Emprunts et Notifications sur un cluster K3s.

## Architecture Kubernetes

```text
Namespace: projet4
├── mysql-catalogue      (Deployment + PVC + Service)
├── mysql-emprunts       (Deployment + PVC + Service)
├── notification-db      (Deployment + PVC + Service)
├── app-catalogue        (Deployment + NodePort 30080)
├── app-emprunts         (Deployment + NodePort 30081)
└── notification-service (Deployment + NodePort 30082)
```

Les noms de services correspondent a ceux utilises en Phase 2 (Docker Compose) pour conserver la communication inter-services.

## Prerequis

- Cluster K3s operationnel (installe via Phase 3 Vagrant ou localement)
- `kubectl` configure
- Docker pour construire les images

## Deploiement

Depuis la racine du projet :

```bash
bash scripts/deploy-k3s.sh
```

Le script :

1. Construit les 3 images applicatives (`projet4/*:local`)
2. Importe les images dans K3s (si disponible)
3. Applique les manifests avec `kubectl apply -k k8s/`
4. Attend le demarrage des deploiements

## Verification

```bash
kubectl get all -n projet4
kubectl logs -n projet4 deployment/app-emprunts
```

## Test de la communication

1. Creer un emprunt avec une date de retour depassee :

```http
POST http://localhost:8081/emprunts/api/emprunts
Content-Type: application/json

{
  "etudiantId": 1,
  "documentId": 1,
  "dateRetourPrevue": "2026-06-20"
}
```

2. Declencher la verification des retards :

```http
POST http://localhost:8081/emprunts/api/emprunts/verifier-retards
```

3. Consulter les notifications generees :

```http
GET http://localhost:8082/notification-service/api/notifications
```

## URLs des services

| Service       | URL |
|---------------|-----|
| Catalogue     | http://localhost:8080/bibliotheque-catalogue-projet/api/catalogue |
| Emprunts      | http://localhost:8081/emprunts/api/emprunts |
| Notifications | http://localhost:8082/notification-service/api/notifications |

## Suppression

```bash
bash scripts/undeploy-k3s.sh
```

## Fichiers principaux

```text
k8s/
  namespace.yaml
  secrets.yaml
  configmaps.yaml
  mysql-catalogue.yaml
  mysql-emprunts.yaml
  mysql-notifications.yaml
  app-catalogue.yaml
  app-emprunts.yaml
  app-notifications.yaml
  kustomization.yaml
scripts/
  deploy-k3s.sh
  undeploy-k3s.sh
  vagrant-up-and-deploy.sh
```

## Deploiement complet (Phase 3 + 4)

```bash
bash scripts/vagrant-up-and-deploy.sh
```

Cette commande demarre la VM Vagrant, installe Docker/K3s, construit les images et deploie le stack complet.
