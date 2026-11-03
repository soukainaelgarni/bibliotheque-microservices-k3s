#!/usr/bin/env bash
set -euo pipefail

echo "==> Installation de K3s Master..."

# Supprimer le token precedent s'il existe
rm -f /opt/projet4/node-token
rm -f /opt/projet4/master_key.pub

# Installer K3s en mode server
if ! command -v k3s >/dev/null 2>&1; then
  curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="--write-kubeconfig-mode 644 --node-ip=192.168.56.10 --bind-address=192.168.56.10 --advertise-address=192.168.56.10 --flannel-iface=enp0s8" sh -
fi

systemctl enable k3s
systemctl start k3s

# Rendre kubectl utilisable par vagrant
mkdir -p /home/vagrant/.kube
cp /etc/rancher/k3s/k3s.yaml /home/vagrant/.kube/config
chown -R vagrant:vagrant /home/vagrant/.kube
chmod 600 /home/vagrant/.kube/config

echo 'export KUBECONFIG=/home/vagrant/.kube/config' >> /home/vagrant/.bashrc

# Generer des cles SSH pour vagrant pour communiquer avec le worker
if [ ! -f /home/vagrant/.ssh/id_rsa ]; then
  ssh-keygen -t rsa -N "" -f /home/vagrant/.ssh/id_rsa
  chown vagrant:vagrant /home/vagrant/.ssh/id_rsa*
fi

# Copier la cle publique dans le dossier partage pour le worker
cp /home/vagrant/.ssh/id_rsa.pub /opt/projet4/master_key.pub
chown vagrant:vagrant /opt/projet4/master_key.pub

# Attendre que le token K3s soit genere
echo "==> Attente du token K3s..."
while [ ! -f /var/lib/rancher/k3s/server/node-token ]; do
  sleep 1
done

# Copier le token K3s dans le dossier partage
cp /var/lib/rancher/k3s/server/node-token /opt/projet4/node-token
chown vagrant:vagrant /opt/projet4/node-token

echo "==> K3s Master installe : $(k3s --version | head -n1)"
