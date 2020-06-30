# torrent-scrapper
My lazy torrent scrapper. Use with caution ☢️ Only for legal files.

## Environnement de build standalone

Si tu souhaites te dispenser d'un IDE et de gérer le projet en CLI, tu es au bon endroit ! 🤓

### Installation native

Peu importe le système d'exploitation, il est nécessaire d'installer certains outils :

1. Installation de **Gradle**
2. Installation du **JDK**

### Image Docker

L'idée est d'utiliser l'[image Docker officielle de Gradle](https://hub.docker.com/_/gradle) pour compiler et exécuter l'application.
Cela évite de gérer les dépendances avec la **JVM**.

La solution la plus simple consiste à créer un **container** que l'on pourra réutiliser à volonté :

* Conservation des variables d'environnement
* Instantiation d'un seul daemon **Gradle**

> Bien évidemment, **Docker** est requis pour ce type d'installation. 🤠

#### Création du container

Il faut lancer un terminal à partir d'ici et exécuter les commandes suivantes :
(Pour Windows, ce nouveau [terminal](https://github.com/Microsoft/Terminal) est recommandé, ou PowerShell)

```bash
# Création et connexion à un container Gradle nommé "gradle-env" :
# 1. Téléchargement de l'image `gradle:latest`
# 2. Création d'un volume partagé avec l'hôte pour accéder à l'arborescence du projet
# 3. Redirection du port `8080` vers celui de l`hôte
# 4. Connexion à la session `gradle`
# 5. Lancement d'un terminal `bash` en intéractif
# Unix
docker run --name gradle-env -u gradle -it -v `pwd`:/home/gradle/project -w /home/gradle/project -p 8080:8080 gradle bash
# Windows
docker run --name gradle-env -u gradle -it -v ${pwd}:/home/gradle/project -w /home/gradle/project -p 8080:8080 gradle bash
# Debug de la CLI Gradle
gradle -v
# Se déconnecter de la session
exit
```

Le *container* `gradle-env` doit à présent se retrouver arrêté, on peut le vérifier comme suit :

```bash
# Affichage de tous les containers
docker ps -a
```

> Lorsque l'on se déconnecte de la session avec `exit`,le *container* s'arrête automatiquement.

#### Lancement et connexion au container

Une fois que le *container* `gradle-env` existe, on peut le relancer n'importe quand et de n'importe où :

```bash
# Lancement du container en mode détaché
docker start gradle-env
# Connexion au terminal du container
docker attach gradle-env
```

La CLI de `Gradle` est à nouveau opérationnelle.

### Exécution de l'application

```bash
# Compilation (si nécessaire) et exécution de l'application sur la JVM
gradle run
```

Crédits @ [Clément LAVERGNE](https://github.com/ClementLavergne) ⭐️

## Lancer l'interface Web

L'interface Web se lance dans un container séparé. Il faut donc que les deux containers tournent pour que l'application complète fonctionne.

```shell
# 1. Se placer à la racine du projet
# 2. Lancer la commande suivante pour créer l'image et le container
docker build -f vuejs-dockerfile -t scrapper-client ./scrapper-client
# 3. Run du container
docker run -it -p 8080:8080 --rm --name scrapper-client-1 scrapper-client
# 4. Se connecter à l'adresse locale
http://127.0.0.1:8080/
# 5. Enjoy 🤡
```
