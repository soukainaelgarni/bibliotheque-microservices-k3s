#!/usr/bin/env bash
set -euo pipefail

VAGRANT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../vagrant" && pwd)"
cd "$VAGRANT_DIR"

echo "==> Demarrage des VMs Vagrant (Phase 3)..."
vagrant up

echo
echo "==> Deploiement K3s dans le cluster (Phase 4)..."
vagrant ssh master -c "cd /opt/projet4 && bash scripts/deploy-k3s.sh"

echo
echo "==> Infrastructure prete."
echo "Acces depuis la machine hote (via Ingress Controller - Port 8085):"
echo "  Catalogue     -> http://localhost:8085/bibliotheque-catalogue-projet/api/catalogue"
echo "  Emprunts      -> http://localhost:8085/emprunts/api/emprunts"
echo "  Notifications -> http://localhost:8085/notification-service/api/notifications"
echo
echo "Acces depuis la machine hote (via NodePorts direct):"
echo "  Catalogue     -> http://localhost:8080/bibliotheque-catalogue-projet/api/catalogue"
echo "  Emprunts      -> http://localhost:8081/emprunts/api/emprunts"
echo "  Notifications -> http://localhost:8087/notification-service/api/notifications"
