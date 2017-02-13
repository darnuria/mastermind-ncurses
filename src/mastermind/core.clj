(ns mastermind.core
  (:gen-class))

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
    (reduce (fn [[x y] c] (do (draw-block scr x  y c) [(+ x 6) y])) [x (inc y)] code)
    (s/put-string scr x (+ y 2)
                  "+-----+-----+-----+-----+-----+-----+" {:bg :default})))

(defn welcome-screen
  [scr]
  (do
    (s/put-string scr 0 1 "MasterMind - NCurses")
    (s/put-string scr 0 2 "Utilisez j, k pour changer une couleur.")
    (s/put-string scr 0 3 "h, l pour changer de case.")
    (s/put-string scr 0 4 "Enter pour la soumettre.")))

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
           trial [:red :blue :green :yellow :white :black]
           colors [:red :blue :green :yellow :white :black]
           color-nb { :red 0 :blue 1 :green 2 :yellow 3 :white 4 :black 5 }]
       (loop [x-cursor 0
              trial trial
              indication (g/indications code trial)]
         (do
           (welcome-screen scr)
           (draw-code scr x y trial)
           (s/move-cursor scr (nth pos x-cursor) (inc y))
           (s/redraw scr))
         (let [
               f #(get color-nb (nth trial %))
               p (case (s/get-key-blocking scr)
                   \h (let [x (mod (dec x-cursor) 6)] [x (f x)])
                   \l (let [x (mod (inc x-cursor) 6)] [x (f x)])
                   \j [x-cursor (mod (dec (f x-cursor)) 6)]
                   \k [x-cursor (mod (inc (f x-cursor)) 6)]
                   :enter nil
                  ; '(if
                  ;            (seq
                  ;             (filter
                  ;              (g/filtre-indications code trial indication)
                  ;              #(not= :good %)))
                  ;          [x-cursor y-cursor]
                  ;          :found)
                   \q nil ; Quit
                   :default [x-cursor (f x-cursor)]
                   )
               [x-cur y-cur] p]
           (when (some? p)
             (let [ color (nth colors y-cur)
                    trial (assoc trial x-cur color)
                    indication (g/indications code trial)]
                 (recur x-cur trial indication)))))))))
; (do
;   (s/put-string 0 0 "win press any key to quit.")
;   (s/redraw scr)
;   (s/get-key-blocking scr))

