(ns testboard.handler
  (:use compojure.core
        [hiccup core page]
        [hiccup.middleware :only (wrap-base-url)]
        [testboard.compojure-macro :only (|-|)]
        testboard.view)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :refer :all]))

;;(editable-div-page-result text)

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/formattext" [] (format-text-page))
  (GET "/popupdatepicker" [] (popup-date-picker-page))
  (GET "/editablediv" [] (editable-div-page))
  (|-| editabledivresult ?text
       (editable-div-page-result text))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))
