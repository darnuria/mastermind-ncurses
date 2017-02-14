(ns mastermind.game)

;; Copyright © 2017, 2017 Axel Viala
;;
;; This file is part of MasterMind NCurses.
;;
;; MasterMind NCurses is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.
;;
;; MasterMind NCurses is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.
;;
;; You should have received a copy of the GNU General Public License
;; along with MasterMind NCurses.  If not, see <http://www.gnu.org/licenses/>.

;; ## Question 1 : tirage du code secre
(defn code-secret
  [n]
  (let [couleurs [:red :blue :green :yellow :black :white]]
    (repeatedly n #(rand-nth couleurs))))

;; ## Question 2 : indications
(defn indications
  "Retourne une sequence correspondante au test du
  code-secret avec l'essai. Si un symbole existe ailleurs,
  dans le code secret on à :color si c'est bon on à :good sinon :bad."
  [code trial]
  (map (fn [c t]
         (if (= (compare c t) 0)
           :good
           (if (first (filter #(= (compare % t) 0) code))
             :color
             :bad)))
       code trial))

;; ## Question 3 : fréquences
(defn frequences
  [s]
  (reduce (fn [freq e]
            (update freq e (fn [old] (if (some? old) (+ old 1) 1))))
          {} s))

;; ## Question 4 : fréquences disponibles
(defn freqs-dispo
  [code indication]
  (let [f-code (frequences code)]
    (reduce
     (fn [acc [c i]]
       (update acc c
               (fn [old] (if (= i :good) (dec old) old))))
     f-code (map vector code indication))))

;; ## Question 5 : filtrer par cadinalité (+ difficile)
(defn filtre-indications
  [code trial indication]
  (let [f-disp (freqs-dispo code indication)]
    (first
     (reduce
      (fn [[acc f-corrected] [c i]]
        (case i
          :good  [(conj acc i) (update f-corrected c dec)]
          :bad   [(conj acc i) f-corrected]
          :color (let [indic (if (>= (c f-corrected) 0) :color :bad)]
                   [(conj acc indic) (update f-corrected c dec)])))
      [[] f-disp] (map vector trial indication)))))

;; ## Questions subsidiaire
;; Realiser un solveur.
;; ## Projet
;; Faire un projet complet avec interface CLI pour lundi 13 février.
