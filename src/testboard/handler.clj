(ns testboard.handler
  (:use compojure.core
        [hiccup core page]
        [hiccup.middleware :only (wrap-base-url)]
        [testboard.compojure-macro :only (|-|)]
        [clojure.string :only (blank?)]
        testboard.editable-div
        testboard.form-builder
        testboard.grid-builder
        testboard.wall
        testboard.view )
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cheshire.core :refer :all]))

;;(editable-div-page-result text)

(defroutes app-routes
  (GET "/" [] (index-page))
  (GET "/popupdatepicker" [] (popup-date-picker-page))
  (GET "/editablediv" [] (editable-div-page))
  (GET "/formattext" [] (format-text-page))
  (GET "/formbuilderpage" [] (form-builder-page))
  (GET "/gridbuilderpage" [] (grid-builder-page))
  (GET "/wallpage" [subjectId] (wall-page subjectId))
  (|-| editabledivresult ?text ?itemId
       (editable-div-page-result text itemId))
  (|-| formbuilderpost ?today ?firstName
       (form-builder-post today firstName))
  (|-| gridbuilderdata ?page ?sortBy ?descending
       (let [sort (if (blank? sortBy) "firstName" sortBy)
             is-descending (Boolean/valueOf descending)]
         (grid-builder-data page sort is-descending)))
  (|-| wallpagedata ?subjectId ?page
       (wall-page-data (Integer/parseInt subjectId) (Integer/parseInt page)))
  (|-| wallpagepost  ?entryTextbox ?subjectId
       (wall-page-post entryTextbox subjectId))
  (|-| wallpageedit  ?text ?itemId
       (wall-page-edit (Integer/parseInt itemId) text))
  (|-| wallpagedelete  ?id
       (wall-page-delete (Integer/parseInt id)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
      (wrap-base-url)))
;;[faker :only (z)]
