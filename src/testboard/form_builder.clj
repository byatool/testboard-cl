(ns testboard.form-builder
  (:use
   [hiccup core page]
   [clojure.string :only (join)]
   [cheshire.core :only (generate-string)]
   [testboard.utility :only (previous-page next-page to-key)]
   [testboard.clojure-macro :only (--|)]))

(defn form-builder-post [username first-name]
  (generate-string 
   {:MessageItems [{:Message (join [username first-name]) :MessageType "info"}]}))
