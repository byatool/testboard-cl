(ns testboard.utility
  (:use
   [clojure.string :only (join capitalize blank?)]))

(defn previous-page [page]
  (if (> page 0)
    (- page 1 )
    page))

(defn next-page [page total-count-of-pages]
  (if (< page (- total-count-of-pages 1))
    (+ 1 page)
    page))


(defn to-key [sortBy]
  (keyword (str (capitalize (first sortBy)) (join (rest sortBy)))))
