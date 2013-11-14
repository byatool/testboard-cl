(ns testboard.clojure-macro
  (:use compojure.core))

(defmacro --| [& rest]
  `((partial ~@rest)))
