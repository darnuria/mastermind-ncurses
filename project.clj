(defproject mastermind "0.1.0-SNAPSHOT"
  :description "mastermindNcurses Jeu de mastermind en Ncurses"
  :url "..."
  :license {:name "GNU Public Licence v3"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-lanterna "0.9.4"]]
  :main ^:skip-aot mastermind.core
  :target-path "target/%s"
  :profiles {
             :uberjar {:aot :all}
             :dev {:dependencies [[midje "1.8.3" :exclusions [org.clojure/clojure]]
                                  [org.clojure/tools.nrepl "0.2.12"]]
                   :plugins [[lein-midje "3.2.1"]]}
             :midje {}})
