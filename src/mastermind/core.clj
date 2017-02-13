(ns mastermind.core
  (:gen-class))

(require '[lanterna.screen :as s])
(require '[mastermind.game :as g])

(defn welcome-screen
  [scr]
  (do
    (s/put-string scr 0 2 "MasterMind")
    (s/put-string scr 0 3 "Utilisez h,j,k,l pour preparer une combinaison")
    (s/put-string scr 0 4 "Enter pour la soumettre.")
    (s/put-string scr 0 12 "+-----------------------------------+"
                  {:bg :default})
    (s/put-string scr 0 13  "|" {:bg :default})
    (s/put-string scr 1 13  "     " {:bg :green})
    (s/put-string scr 6 13  "|" {:bg :default})
    (s/put-string scr 7 13 "     " {:bg :red})
    (s/put-string scr 12 13  "|" {:bg :default})
    (s/put-string scr 13 13 "     " {:bg :yellow})
    (s/put-string scr 18 13  "|" {:bg :default})
    (s/put-string scr 19 13 "     " {:bg :blue})
    (s/put-string scr 24 13  "|" {:bg :default})
    (s/put-string scr 25 13 "     " {:bg :white})
    (s/put-string scr 30 13  "|" {:bg :default})
    (s/put-string scr 31 13 "     " {:bg :black})
    (s/put-string scr 36 13  "|" {:bg :default})
    (s/put-string scr 0 14 "+-----------------------------------+"
                  {:bg :default})))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [scr (s/get-screen :text :unix)]
    (s/in-screen
     scr
     (do
       (welcome-screen scr)
       (s/redraw scr)
       (s/get-key-blocking scr)))))

