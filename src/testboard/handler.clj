(ns testboard.handler
  (:use compojure.core
        [hiccup core page]
        [hiccup.middleware :only (wrap-base-url)]
        testboard.view)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :refer :all]))


(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/formattext" [] (format-text-page))
  (GET "/popupdatepicker" [] (popup-date-picker-page))
  (POST "/postit" []
        (generate-string {:test {:name "ad"}}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))
