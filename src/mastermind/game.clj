(ns mastermind.game
  (:use midje.sweet))

;; ## Question 1 : tirage du code secre
(defn code-secret
  [n]
  (let [couleurs [:rouge :bleu :vert :jaune :noir :blanc]]
    (repeatedly n #(rand-nth couleurs))))


(fact "Le `code-secret` est bien composé de couleurs."
      (every? #{:rouge :bleu :vert :jaune :noir :blanc}
              (code-secret 4))
      => true)

(fact "Le `code-secret` a l'air aléatoire."
      (> (count (filter true? (map not=
                                   (repeatedly 20 #(code-secret 4))
                                   (repeatedly 20 #(code-secret 4)))))
         0)
      => true)


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


(fact "`indications` sont les bonnes."
      (indications [:rouge]
                   [:vert])
      => [:bad]
      (indications [:rouge]
                   [:rouge])
      => [:good]
      (indications [:rouge :rouge]
                   [:vert  :rouge])
      => [:bad :good]
      (indications [:rouge :rouge :vert :bleu]
                   [:vert :rouge :bleu :jaune])
      => [:color :good :color :bad]
      (indications [:rouge :rouge :vert :bleu]
                   [:bleu :rouge :vert :jaune])
      => [:color :good :good :bad]
      (indications [:rouge :rouge :vert :bleu]
                   [:rouge :rouge :vert :bleu])
      => [:good :good :good :good]
      (indications [:rouge :rouge :vert :vert]
                   [:vert :bleu :rouge :jaune])
      => [:color :bad :color :bad])

;; ## Question 3 : fréquences
(defn frequences
  [s]
  (reduce (fn [freq e] (update freq e (fn [old] (if (some? old) (+ old 1) 1))))
          {} s))


(fact "les `frequences` suivantes sont correctes."
      (frequences [:rouge :rouge :vert :bleu :vert :rouge])
      => (just {:rouge 3 :vert 2 :bleu 1})
      (frequences [:rouge :vert :bleu])
      => (just {:rouge 1 :vert 1 :bleu 1})
      (frequences [1 2 3 2 1 4])
      => (just {1 2, 2 2, 3 1, 4 1}))

;; ## Question 4 : fréquences disponibles
(defn freqs-dispo
  [code indication]
  (let [f-code (frequences code)]
    (reduce
     (fn [acc [c i]]
       (update acc c
               (fn [old] (if (= i :good) (dec old) old))))
     f-code (map vector code indication))))

(fact "Les fréquences disponibles de `freqs-dispo` sont correctes."
      (freqs-dispo [:rouge :rouge :bleu :vert :rouge]
                   [:good :color :bad :good :color])
      => {:bleu 1, :rouge 2, :vert 0})

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

(fact "Le `filtre-indications` fonctionne bien."
      (filtre-indications [:rouge :rouge :vert :bleu]
                          [:vert :rouge :bleu :jaune]
                          [:color :good :color :bad])
      => [:color :good :color :bad]
      (filtre-indications [:rouge :vert :rouge :bleu]
                          [:rouge :rouge :bleu :rouge]
                          [:good :color :color :color])
      => [:good :color :color :bad])

;; ## Questions subsidiaire
;; Realiser un solveur.
;; ## Projet
;; Faire un projet complet avec interface CLI pour lundi 13 février.
