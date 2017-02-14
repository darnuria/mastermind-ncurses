(ns mastermind.core
  (:gen-class))

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

(require '[lanterna.screen :as s])
(require '[mastermind.game :as g])

(defn draw-block
  [scr x y color]
  (do
    (s/put-string scr x y "|" {:bg :default})
    (s/put-string scr (inc x) y "     " {:bg color})
    (s/put-string scr (+ 6 x) y "|" {:bg :default})))

(defn draw-code
  [scr x y code]
  (do
    (s/put-string scr x y
                  "+-----+-----+-----+-----+-----+-----+" {:bg :default})
    (reduce (fn [[x y] c]
              (do (draw-block scr x  y c)
                  [(+ x 6) y]))
            [x (inc y)]
            code)
    (s/put-string scr x (+ y 2)
                  "+-----+-----+-----+-----+-----+-----+" {:bg :default})))

(defn draw-indication
  [scr x y indication]
  (s/put-string scr x (+ y 3) "                                               ")
  (s/put-string scr x (+ y 3) (clojure.string/join " " indication)))

(defn welcome-screen
  [scr]
  (do
    (s/put-string scr 0 1 "MasterMind - NCurses")
    (s/put-string scr 0 2 "Utilisez j, k pour changer une couleur.")
    (s/put-string scr 0 3 "h, l pour changer de case.")
    (s/put-string scr 0 4 "Enter pour la soumettre.")))

(defn nb->color
  [n]
  (let [colors [:red :blue :green :yellow :white :black]]
    (nth colors n)))

(defn color->nb
  [color]
  (let [color-nb { :red 0 :blue 1 :green 2 :yellow 3 :white 4 :black 5 }]
    (get color-nb color)))

(defn color-pred
  [color]
  (nb->color (mod (dec (color->nb color)) 6)))

(defn color-suiv
  [color]
  (nb->color (mod (inc (color->nb color)) 6)))

(defn pos-pred
  [pos]
  (mod (dec pos) 6))

(defn pos-suiv
  [pos]
  (mod (inc pos) 6))

(defn update-x-color
  [key x-cursor code]
  (defn c [x] (nth code x))
  (case key
    \h (let [x (pos-pred x-cursor)] [x (c x)])
    \l (let [x (pos-suiv x-cursor)] [x (c x)])
    \j [x-cursor (color-pred (c x-cursor))]
    \k [x-cursor (color-suiv (c x-cursor))]
    \q (throw (Exception. "quiting!"))
    [x-cursor (c x-cursor)]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [scr (s/get-screen :text :unix)]
    (s/in-screen
     scr
     (let [x 0
           y 6
           code (g/code-secret 6)
           pos   [3 9 15 21 27 33]
           trial [:red :blue :green :yellow :white :black]]
       (do
         (welcome-screen scr)
         (draw-code scr x y trial)
         (s/move-cursor scr (nth pos 0) (inc y))
         (s/redraw scr))
       (try ; Pour quitter sans laisser le shell dans un etat incorrect.
         (loop [x-cursor 0
                trial trial]
           (let [[x-cur color] (update-x-color (s/get-key-blocking scr)
                                               x-cursor
                                               trial)
                 trial (assoc trial x-cur color)
                 indic (g/indications code trial)
                 indic-filtered (g/filtre-indications code trial indic)]
             (do
               (welcome-screen scr)
               (draw-code scr x y trial)
               (draw-indication scr 0 (+ y 3) indic-filtered)
               (s/move-cursor scr (nth pos x-cur) (inc y))
               (s/redraw scr))
             (if (seq (filter #(not= :good %) indic))
               (recur (long x-cur)
                      trial)
               (do
                 (s/put-string scr 0 (+ y 4) "Win! Press any key to quit.")
                 (s/redraw scr)
                 (s/get-key-blocking scr)))))
         (catch Exception e nil))))))

