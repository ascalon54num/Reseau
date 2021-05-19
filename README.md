# Projet Réseau

## Le projet
***
Ce projet a été réalisé dans le but de créer son propre serveur HTTP. 
L'objectif étant de pouvoir héberger des sites dans les ressources du projet pour les consulter depuis un navigateur.
Comme pourrait le faire un serveur Apache, ce projet permet par le biai de l'URL du navigateur de renseigner une adresse IP avec le nom du site hébergé pour y accéder.

Pour ce qui est de la fonctionnalité bonus, ce projet intègre la compression en Gzip des fichiers javascript et feuilles de style.
Cela permet d'économiser la quantité de donnée à transmettre pour le bon affichage du site.
Le navigateur se charge ensuite de décompresser le fichier pour qu'il soit correctement affiché à l'utilisateur.

## Technologies
***
* [Java](https://openjdk.java.net/)

## Prerequis
***
Il faut avoir java d'installé pour pouvoir executer des ``.jar``

## Installation
***
### Ajouter des faux DNS
1. Rendez-vous dans le fichier suivant (sur Windows)
``C:\Windows\System32\drivers\etc\hosts``
  
 
2. Ajouter ceci
```
127.0.0.1	verti.fr
127.0.0.1	dopetrope.fr
```
Une fois le serveur lancé, il vous suffira de saisir un de ces noms de domaine 
pour être redirigé sur le bon site.
Le nom de domaine étant le nom du dossier contenant le site dans les ressources du serveur.

Il est toutefois possible d'accéder au site par le biais de l'URL entière :
``http://127.0.0.1/verti/index.html``


## Utilisation
***
Il suffit de double cliquer sur le fichier ```app.bat``` dans le dossier racine.

Le serveur devrait se lancer et vous pouvez tester son fonctionnement en tapant ``127.0.0.1`` dans 
votre navigateur.

Quand vous arriverez sur la page, il faudra renseigner un utilisateur et un mot de passe
```
Login ==> ascalon
MDP ==> test
```

Amusez vous bien avec ce serveur Miapache (Miage Apache)


## Configuration
***
Dans le fichier ``Reseau/config/config.conf`` il y a certaines configurations
```lombok.config
address = 127.0.0.1     # Adresse du serveur à taper dans l'URL du navigateur
port = 80               # Port de communication du serveur
hosts_dir = html        # Emplacement du dossier des sites (ne pas modifier sauf si vous savez ce que vous faites (on en doute pas !))
```
Le changement d'une de ces options nécessite de re-builder le *.jar* en le plaçant 
au même endroit que le précédent.

## Arborescence
***
```
Reseau
│   Server.java
│   ServiceClient.java
│
├───config
│       config.conf
│
├───html
│   │   .htpasswd
│   │   index.html
│   │
│   ├───dopetrope
│   │           ...
│   │
│   └───verti
│           ...
│
├───utils
│       ConfigReader.java
│       SecurityReader.java
│       SiteReader.java
│
└─── Reseau.jar
```

## Auteur
***
* MAIRESSE Stéphane
* MERCIER Clément
* NASRI Manel

## Licence
***
Ce projet est distribué sous une licence WTFPL
(voir [license](LICENSE.txt) pour plus de détails)

