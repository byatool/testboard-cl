(defproject testboard "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [lib-noir "0.7.0"]
                 [cheshire "5.2.0"]
                 [faker "0.2.2"]
                 [org.clojure/tools.trace "0.7.6"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler testboard.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})

