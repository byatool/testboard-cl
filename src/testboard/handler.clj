(ns testboard.handler
  (:use compojure.core
        [hiccup core page]
        [hiccup.middleware :only (wrap-base-url)]
        [testboard.compojure-macro :only (|-|)]
        [clojure.string :only (blank?)]
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
  (GET "/gridbuilderpage" [] (grid-builder-page))
  (|-| editabledivresult ?text ?itemId
       (editable-div-page-result text itemId))
  (|-| gridbuilderdata ?page ?sortBy
       (let [sort (if (blank? sortBy) "firstName" sortBy)]
         (grid-builder-data page sort)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))
;;[faker :only (z)]
