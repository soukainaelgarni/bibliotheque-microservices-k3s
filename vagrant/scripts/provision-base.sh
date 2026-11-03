#!/usr/bin/env bash
set -euo pipefail

export DEBIAN_FRONTEND=noninteractive

echo "==> Mise a jour du systeme..."
apt-get update -y
apt-get upgrade -y

echo "==> Installation des outils de base..."
apt-get install -y \
  apt-transport-https \
  ca-certificates \
  curl \
  gnupg \
  lsb-release \
  net-tools \
  jq \
  unzip

echo "==> Configuration du dossier projet..."
mkdir -p /opt/projet4
chown -R vagrant:vagrant /opt/projet4

echo "==> Provisionnement de base termine."
