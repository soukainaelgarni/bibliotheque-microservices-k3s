#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# Les images Docker doivent etre construites sur la machine hote et importees prealablement dans K3s.

echo "==> Deploiement Kubernetes (namespace projet4)..."
kubectl apply -k k8s/

echo "==> Attente du demarrage des pods..."
kubectl rollout status deployment/mysql-catalogue -n projet4 --timeout=180s || true
kubectl rollout status deployment/mysql-emprunts -n projet4 --timeout=180s || true
kubectl rollout status deployment/notification-db -n projet4 --timeout=180s || true
kubectl rollout status deployment/notification-service -n projet4 --timeout=300s || true
kubectl rollout status deployment/app-catalogue -n projet4 --timeout=300s || true
kubectl rollout status deployment/app-emprunts -n projet4 --timeout=300s || true

echo
echo "==> Deploiement termine."
kubectl get pods,svc -n projet4
echo
echo "URLs (NodePort):"
echo "  Catalogue     -> http://localhost:8080/bibliotheque-catalogue-projet/api/catalogue"
echo "  Emprunts      -> http://localhost:8081/emprunts/api/emprunts"
echo "  Notifications -> http://localhost:8087/notification-service/api/notifications"
echo
echo "URLs (Ingress Controller - via port 8085):"
echo "  Catalogue     -> http://localhost:8085/bibliotheque-catalogue-projet/api/catalogue"
echo "  Emprunts      -> http://localhost:8085/emprunts/api/emprunts"
echo "  Notifications -> http://localhost:8085/notification-service/api/notifications"
