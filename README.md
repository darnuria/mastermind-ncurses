# MasterMind NCurses

Jeu en Ncurses devellopé pour le cours de
3I020 - Programmation Déclarative - 2016-2017 à l'UPMC.

Auteur Axel Viala

Utilise la bibliotheque [clojure-lanterna](https://multimud.github.io/clojure-lanterna/)

## Usage

Controles: h, j, k, l pour jouer; q pour quitter

```
$ java -jar mastermind-0.1.0-standalone.jar [args]
```

## Bugs

Obligation d'utiliser une exeception pour sortir de la game loop via la touche `q`.
`java.lang.System.exit` Ne laisse pas le terminal dans un etat convenable.
Voir `lanterna-clojure.screen/in-screen` macro.


"Contours" du terminal qui affichent des `X` verts à investiguer.

## License

Copyright © 2017 Axel Viala
GPLv3 see COPYING

