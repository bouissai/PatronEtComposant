# PatronEtConception #

Groupe : 5_2
Binôme : GUERBAA Rayan  & BOUISSA Ilyass

- - - -

### Sommaire ###
* Présentation du projet
* Organisation du projet
* Conception
* Fonctionnalités implémentées 
* Avancée du projet 
* Conclusion

- - - -

### Présentation du projet ###
Avant de tester le projet, nous allons vous présenter le projet sur lequel nous travaillons pour notre cours de Patron et Composant.
Il s'agit d'une interface graphique en java qui permet de dessiner des formes ainsi que de les déplacer.
Cette interface est développée en java à l'aide de la librérie JDrawing.

### Organisation du projet ###
Pour ce projet, en binôme nous avons avancé cette interface par incrément, dans lequel nous devions ajouter de nouvelles fonctionnalités chaque semaine.
Pour ce faire, nous avons dû adapter notre projet à chaque incréments en faisant appelle au différents partons de conception à adapter selon nos besoins.

- - - -

### Conception ###
Durant le projet, nous sommes parti avec un projet déjà existant que nous devions entièrement adapter et corriger.
Pour cela, nous avons passé les premières séances à développer des petites fonctionnalités, comprendre le code existant et mettre en place un premier patron.

A partir d'un certain avancement dans le projet, nous avons conçu différents diagrammes pour illustrer les patrons composants que nous avons utilisé.

* Diagramme de classe 
Dans un 1er temps nous avons implementé ces 2 diagramme pour représenté nos premières idées de conception sur l'utilisation du patron composant et patron visitor
- diagramme de classe 1 : notre projet actuel
<img src="document_conception/DC_1.png" alt="drawing"/>
- diagramme de classe 2 : ce que l'on souhaite faire
  <img src="document_conception/DC_2.png" alt="drawing"/>

Ensuite, nous avons avancé dans les incréments et nous devions ajouter de nouvelles fonctionnalités dans notre projet.
Pour cela, nous avons avancé notre projet et conçu un nouveau diagramme de classe correpondant à l'état final de notre projet et également au premier rendu.
- diagramme de classe 3 : premier rendu
  <img src="document_conception/Diagramme de Classe Projet PC V1.svg" alt="drawing"/>
  
Ensuite, nous avons effectué des modification avec l'ajout des fonctionnalité undo et l'ajout d'une nouvelle image.
Nous avons donc repris le diagramme précédent que nous avons adapté
- diagramme de classe 4 : version finale et adaptée du projet pour le dernier rendu
  <img src="document_conception/Diagramme de Classe Projet PC V2.svg" alt="drawing"/>

* Diagramme de sequence
Nous avons conçu plusieurs diagrammes de séquences pour représenter certaines fonctionnalités
- Diagramme de séquence de la fonctionnalité Movable
  <img src="document_conception/DS_MOVABLE.png" alt="drawing"/>
  - Diagramme de séquence de la fonctionnalité déplacer objet
  <img src="document_conception/deplacerObjet.svg" alt="drawing"/>
  - Diagramme de séquence de la fonctionnalité déplacement d'une forme
  <img src="document_conception/diagramme_sequence.svg" alt="drawing"/>
  - Diagramme de séquence de la fonctionnalité formation d'un groupe
  <img src="document_conception/group_selection.svg" alt="drawing"/>


* Diagramme de composant
comme nous n'avons pas implémenté le partage de formes avec un autre binôme, nous n'avons pas conçu de diagramme de composant

- - - -

### fonctionnalités implémentées ###
* reset:

    <img src="ressources_readme/reset.gif" alt="drawing" style="width:200px;"/>

* import

  <img src="ressources_readme/import.gif" alt="drawing" style="width:200px;"/>

* export

  <img src="ressources_readme/export.gif" alt="drawing" style="width:200px;"/>


* dessiner des formes

  <img src="ressources_readme/draw.gif" alt="drawing" style="width:200px;"/>

* déplacer une forme

  <img src="ressources_readme/move.gif" alt="drawing" style="width:200px;"/>

* former un groupe

  <img src="ressources_readme/group.gif" alt="drawing" style="width:200px;"/>

* déplacer un groupe

  <img src="ressources_readme/moveGroup.gif" alt="drawing" style="width:200px;"/>

* former un groupe de groupe

  <img src="ressources_readme/groupOfGroup.gif" alt="drawing" style="width:200px;"/>

* undo (en cours de développement)

  <img src="ressources_readme/undo.gif" alt="drawing" style="width:200px;"/>

- - - -

### Avancée du projet ###
* notre avancé au premier rendu
- [x] patron composite
- [x] patron factory
- [x] patron visiteur
- [x] création de formes
- [x] création de groupes
- [x] création de groupes avec sous groupes
- [x] déplacement de toutes les formes
- [x] export XML et JSON
- [x] import XML
- [x] bouton reset
- [x] refactoring des méthodes pour clean code
- [x] couverture de test à 25%
- [x] documents de conceptions
- [ ] import JSON
- [ ] control Z
- [ ] rapport

* notre avancé au second rendu
- [x] un rapport un peu plus présentable !
- [x] couverture de test passé à 35% 
- [x] ajout d'une nouvelle image
- [x] ajout d'un bouton undo
- [x] adaptation des documents de conceptions avec la nouvelle image

* ce qui nous reste à implémenter
- [ ] monter la couverture de test à 70%
- [ ] récupérer le .jar d'un autre binôme et le tester avec notre code
- [ ] améliorer le undo pour prendre en compte tous les cas (comme le respect de l'ordre de création des objets)
- [ ] faire un redo
- [ ] ...

- - - -

### Conclusion ###
...
