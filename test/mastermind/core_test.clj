(ns mastermind.core-test
  (:use midje.sweet)
  (:require [mastermind.core :refer :all]
            [mastermind.game :refer :all]))

(fact "Le `code-secret` est bien composé de couleurs."
      (every? #{:red :blue :green :yellow :black :white}
              (code-secret 4))
      => true)

(fact "Le `code-secret` a l'air aléatoire."
      (> (count (filter true? (map not=
                                   (repeatedly 20 #(code-secret 4))
                                   (repeatedly 20 #(code-secret 4)))))
         0)
      => true)

(fact "`indications` sont les bonnes."
      (indications [:red]
                   [:green])
      => [:bad]
      (indications [:red]
                   [:red])
      => [:good]
      (indications [:red :red]
                   [:green  :red])
      => [:bad :good]
      (indications [:red :red :green :blue]
                   [:green :red :blue :yellow])
      => [:color :good :color :bad]
      (indications [:red :red :green :blue]
                   [:blue :red :green :yellow])
      => [:color :good :good :bad]
      (indications [:red :red :green :blue]
                   [:red :red :green :blue])
      => [:good :good :good :good]
      (indications [:red :red :green :green]
                   [:green :blue :red :yellow])
      => [:color :bad :color :bad])

(fact "Les fréquences disponibles de `freqs-dispo` sont correctes."
      (freqs-dispo [:red :red :blue :green :red]
                   [:good :color :bad :good :color])
      => {:blue 1, :red 2, :green 0})

(fact "les `frequences` suivantes sont correctes."
      (frequences [:red :red :green :blue :green :red])
      => (just {:red 3 :green 2 :blue 1})
      (frequences [:red :green :blue])
      => (just {:red 1 :green 1 :blue 1})
      (frequences [1 2 3 2 1 4])
      => (just {1 2, 2 2, 3 1, 4 1}))

(fact "Le `filtre-indications` fonctionne bien."
      (filtre-indications [:red :red :green :blue]
                          [:green :red :blue :yellow]
                          [:color :good :color :bad])
      => [:color :good :color :bad]
      (filtre-indications [:red :green :red :blue]
                          [:red :red :blue :red]
                          [:good :color :color :color])
      => [:good :color :color :bad]

      (filtre-indications [:red :black :black :red    :black :black]
                          [:red :blue  :green :yellow :white :black]
                          [:good :bad  :bad   :bad  :bad   :good])
                       => [:good :bad  :bad   :bad    :bad   :good])


(fact "update-x-color est correcte"
      (fact "update-x-color est correcte pour \\h"
      (update-x-color \h 0 [:red :red :red :red :red :red]) => [5 :red]
      (update-x-color \h 1 [:red :red :red :red :red :red]) => [0 :red]
      (update-x-color \h 2 [:red :red :red :red :red :red]) => [1 :red]
      (update-x-color \h 3 [:red :red :red :red :red :red]) => [2 :red]
      (update-x-color \h 4 [:red :red :red :red :red :red]) => [3 :red]
      (update-x-color \h 5 [:red :red :red :red :red :red]) => [4 :red])
      (fact "update-x-color est correcte pour \\l"
      (update-x-color \l 0 [:red :red :red :red :red :red]) => [1 :red]
      (update-x-color \l 1 [:red :red :red :red :red :red]) => [2 :red]
      (update-x-color \l 2 [:red :red :red :red :red :red]) => [3 :red]
      (update-x-color \l 3 [:red :red :red :red :red :red]) => [4 :red]
      (update-x-color \l 4 [:red :red :red :red :red :red]) => [5 :red]
      (update-x-color \l 5 [:red :red :red :red :red :red]) => [0 :red]
      (fact "update est correcte pour \\j")
      (update-x-color \j 0 [:red :blue :yellow :green :black :white])
      => [0 :black]
      (update-x-color \j 0 [:black :blue :yellow :green :black :white])
      => [0 :white]
      (update-x-color \j 0 [:yellow :blue :yellow :green :black :white])
      => [0 :green]
      (update-x-color \j 0 [:blue :blue :yellow :green :black :white])
      => [0 :red]
      (update-x-color \j 1 [:red :blue :yellow :green :black :white])
      => [1 :red]
      (update-x-color \j 2 [:red :blue :yellow :green :black :white])
      => [2 :green]
      (update-x-color \j 3 [:red :blue :yellow :green :black :white])
      => [3 :blue]
      (update-x-color \j 4 [:red :blue :yellow :green :black :white])
      => [4 :white]
      (update-x-color \j 5 [:red :blue :yellow :green :black :white])
      => [5 :yellow]))
