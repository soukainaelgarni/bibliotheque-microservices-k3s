# Projet 4 - Phase 3 : Infrastructure as Code (Vagrant)

Cette phase provisionne automatiquement une machine virtuelle Ubuntu avec Docker et K3s.

## Prerequis

- [VirtualBox](https://www.virtualbox.org/)
- [Vagrant](https://www.vagrantup.com/)
- Au moins 4 Go de RAM disponibles pour la VM

## Structure

```text
vagrant/
  Vagrantfile
  scripts/
    provision-base.sh      # outils systeme
    provision-docker.sh    # installation Docker
    provision-k3s.sh         # installation K3s
```

## Demarrage de l'infrastructure

Depuis le dossier `vagrant/` :

```bash
cd vagrant
vagrant up
```

Ou depuis la racine du projet :

```bash
bash scripts/vagrant-up-and-deploy.sh
```

## Verification

```bash
cd vagrant
vagrant ssh
kubectl get nodes
docker --version
k3s --version
```

La VM est accessible sur le reseau prive `192.168.56.10`.

## Ports forwards (hote -> VM)

| Service       | Port hote | NodePort VM |
|---------------|-----------|-------------|
| Catalogue     | 8080      | 30080       |
| Emprunts      | 8081      | 30081       |
| Notifications | 8082      | 30082       |

## Commandes utiles

```bash
cd vagrant
vagrant ssh          # connexion SSH
vagrant reload       # redemarrer la VM
vagrant provision    # relancer les scripts de provisionnement
vagrant halt         # arreter la VM
vagrant destroy      # supprimer la VM
```

## Enchainement avec la Phase 4

Une fois la VM demarree, deployer les microservices sur K3s :

```bash
vagrant ssh -c "cd /opt/projet4 && bash scripts/deploy-k3s.sh"
```
