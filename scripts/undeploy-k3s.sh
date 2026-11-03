#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

echo "==> Suppression du deploiement Kubernetes..."
kubectl delete -k k8s/ --ignore-not-found=true

echo "==> Namespace projet4 supprime."
