#!/usr/bin/env bash
set -euo pipefail

echo "==> Installation de K3s Worker..."

# Attendre que le token K3s soit disponible dans le dossier partage
echo "==> Attente du token du master..."
while [ ! -f /opt/projet4/node-token ]; do
  sleep 2
done

# Attendre que la cle publique du master soit disponible
echo "==> Attente de la cle SSH du master..."
while [ ! -f /opt/projet4/master_key.pub ]; do
  sleep 2
done

# Ajouter la cle publique du master aux cles autorisees du worker
mkdir -p /home/vagrant/.ssh
cat /opt/projet4/master_key.pub >> /home/vagrant/.ssh/authorized_keys
chmod 600 /home/vagrant/.ssh/authorized_keys
chown -R vagrant:vagrant /home/vagrant/.ssh

# Installer K3s en mode agent
if ! command -v k3s >/dev/null 2>&1; then
  curl -sfL https://get.k3s.io | K3S_URL="https://192.168.56.10:6443" K3S_TOKEN="$(cat /opt/projet4/node-token)" INSTALL_K3S_EXEC="--node-ip=192.168.56.11 --flannel-iface=enp0s8" sh -
fi

echo "==> K3s Worker installe."
