(ns testboard.grid-builder
  (:use
   [clojure.string :only (join)]
   [testboard.view :only (master-page)]
   [testboard.utility :only (append-return resolve-previous-page resolve-next-page to-key)]
   faker.name
   faker.internet)
  (:require
   [cheshire.core :refer :all]))


;; Utility 

(defn create-user [id]
  {:Id id
   :LastName (last-name)
   :FirstName (first-name)
   :Ssn (clojure.string/join ["111-11-111" id])
   :Email (email)})


(defn create-users []
  (map create-user (range 0 20)))


(def users (create-users))


(defn retrieve-users [page per-page sort descending]
  (take per-page
        (drop (* page 5)
              (sort-by (to-key sort)
                       (if descending
                         #(compare %2 %1)
                         #(compare %1 %2))
                       users))))

;; Get

(defn grid-builder-page []
  (let [script-text (append-return
                     [
                      "var options = {};"
                      "options[src.base.control.gridBuilder.constant.ContainerClass] = 'gridBuilderContainer';"
                      "options[src.base.control.gridBuilder.constant.ContainerId] = 'gridBuilderContainer';"
                      "options[src.base.control.gridBuilder.constant.Url] = '/gridbuilderdata/';"
                      "options[src.base.control.gridBuilder.constant.Parameters] = { 'page': 0 };"
                      "options[src.base.control.gridBuilder.constant.Map] = ["
                      "  { 'headerText': 'Last Name', 'propertyName': 'LastName', 'class': 'short' },"
                      "  { 'headerText': 'First Name', 'propertyName': 'FirstName', 'class': 'short' },"
                      "  { 'headerText': 'Email Address', 'propertyName': 'Email', 'class': 'long' },"
                      "  { 'headerText': 'Social Security Number', 'propertyName': 'Ssn', 'class': 'short' }"
                      "];"
                      "options[src.base.control.gridBuilder.constant.ShowHeader] = true;"
                      "options[src.base.control.gridBuilder.constant.RowClickHandler] = function(row) { "
                      "   alert(row.innerHTML); " ;
                      " };"
                      "var result = src.base.control.gridBuilder.initialize(options);"
                      "document.getElementById('mainContainer')."
                      "  appendChild(result[src.base.control.controlConstant.CreatedControl]);"])]
    (master-page [:div
                  [:div {:id "mainContainer"}]
                  [:pre script-text]
                  [:script
                   script-text ]])))


;; Post

(defn grid-builder-data [page sortBy descending]
  (let [total-count-of-pages (/ (count users) 5)
        previous-page (resolve-previous-page (Integer/parseInt page))
        next-page (resolve-next-page (Integer/parseInt page) total-count-of-pages)]
    (generate-string {:PreviousPage previous-page
                      :NextPage next-page
                      :TotalCountOfPages total-count-of-pages
                      :List (retrieve-users (Integer/parseInt page) 5 sortBy descending)})))
