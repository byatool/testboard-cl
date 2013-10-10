(ns testboard.view
  (:use [hiccup core page]))


(defn master-page [to-inject]
  (html5
   [:head
    [:title "overall title" ]
    (include-css "/css/final.css")
    (include-js "/script/final.js")]
   [:body
    to-inject]))


(defn index-page []
  (master-page [:div
                [:h1 "Welcome"]
                [:a {:href "/formattext"} "format text"]]))

(defn format-text-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script "var holder = document.getElementById('mainContainer'); var datePicker = src.base.control.formatTextAreaDisplay.initialize(document, src.base.control.formatTextAreaDisplay.javascript.format); holder.appendChild(datePicker);"]]))

;; (defn master-page [to-inject]
;;   (html5
;;    [:head
;;     [:title "overall title"]
;;     (include-js "/script/live.js")
;;     (include-css "/css/final.css")]
;;    [:body
;;     to-inject]))


;; (defn index-page []
;;   (master-page [:h1 "Welcome"]))


;; (defn text-format-page
;;   (master-page
;;    [:div
;;     [:div {:id "maincontainer"}]
;;     [:script "var holder = document.getElementById('mainContainer');"]]))



;;[:a {:href "/formattext"} "format text"]
;;:div {:class ".buttonListContainer"}
