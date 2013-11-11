(ns testboard.view
  (:use [clojure.string :only (join capitalize blank?)]
        [hiccup core page]
        [cheshire.core :only (generate-string)]
        faker.name
        faker.internet))


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
                [:a {:href "/editablediv"} "editable div  "]
                [:br]
                [:a {:href "/formattext"} "format text "]
                [:br]
                [:a {:href "/popupdatepicker"} "popup date picker "]]))


(defn format-text-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var FormatTextAreaDisplay_ = src.base.control.formatTextAreaDisplay; "
                 "var holder = document.getElementById('mainContainer'); "
                 "var formatText = FormatTextAreaDisplay_.initialize(document);"
                 "holder.appendChild(formatText);"]]))


(defn popup-date-picker-page []
  (master-page [:div {:id "mainContainer"}
                [:div
                 [:input {:type "text" :id "targetTextbox" :class "floatLeft"}]
                 [:div {:id "datePickerHolder" :class "floatLeft"}]
                 [:div {:class "clearBoth"}]
                 [:script
                  "var DatePicker = src.base.control.popupDatePicker; "
                  "var textboxName = 'targetTextbox'; "
                  "var holderName = 'datePickerHolder'; "
                  "var datePickerOptions = {};"
                  "datePickerOptions[DatePicker.constant.ButtonText] = 'Date';"
                  "datePickerOptions[DatePicker.constant.TextboxName] = textboxName;"
                  "var holder = document.getElementById(holderName);"
                  "var datePicker = DatePicker.create(datePickerOptions);"
                  "holder.appendChild(datePicker);"]]]))


(defn editable-div-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var EditableDiv_ = src.base.control.editableDiv; "
                 "var holder = document.getElementById('mainContainer'); "
                 "var editableDiv = EditableDiv_.initialize('theEditableDiv', 'this is the text', '1', '/editabledivresult/');"
                 "holder.appendChild(editableDiv);"]]))


(defn grid-builder-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var options = {};"
                 "options[src.base.control.gridBuilder.constant.ContainerClass] = 'gridBuilderContainer';"
                 "options[src.base.control.gridBuilder.constant.ContainerId] = 'gridBuilderContainer';"
                 "options[src.base.control.gridBuilder.constant.Url] = '/gridbuilderdata/';"
                 "options[src.base.control.gridBuilder.constant.Parameters] = { 'page': 0 };"
                 "options[src.base.control.gridBuilder.constant.Map] = ["
                 "  { 'headerText': 'Last Name', 'propertyName': 'LastName', 'class': 'short' },"
                 "  { 'headerText': 'First Name', 'propertyName': 'FirstName', 'class': 'short' },"
                 "  { 'headerText': 'Email Address', 'propertyName': 'Email', 'class': 'long' },"
                 "  { 'headerText': 'Social Security Number', 'propertyName': 'Ssn', 'class': 'medium' }"
                 "];"
                 "options[src.base.control.gridBuilder.constant.ShowHeader] = true;"
                 "options[src.base.control.gridBuilder.constant.RowClickHandler] = function(row) { alert(row.innerHTML); };"
                 "var result = src.base.control.gridBuilder.initialize(options);"
                 "document.getElementById('mainContainer').appendChild(result);" ]]))


(defn editable-div-page-result [text id]
  (generate-string {:MessageItems [{:Message (join [text id]) :MessageType "error"}]}))


(defn previous-page [page]
  (if (> page 0)
    (- page 1 )
    page))

(defn next-page [page totalCountOfPages]
  (if (< page (- totalCountOfPages 1))
    (+ 1 page)
    page))

(defn create-user [id]
  {:Id id
   :LastName (last-name)
   :FirstName (first-name)
   :Ssn (clojure.string/join ["111-11-111" id])
   :Email (email)})

(defn create-users []
  (map create-user (range 0 20)))

(def users (create-users))

(defn to-key [sortBy]
  (keyword (str (capitalize (first sortBy)) (join (rest sortBy)))))

(defn retrieve-users [page perPage sortBy descending]
  (take perPage
        (drop (* page 5)
              (sort-by (to-key sortBy)
                       (if descending
                         #(compare %2 %1)
                         #(compare %1 %2))
                       users))))

;;(retrieve-users 0 5 "FirstName")

(defn grid-builder-data [page sortBy descending]
  (let [totalCountOfPages 4
        previousPage (previous-page (Integer/parseInt page))
        nextPage (next-page (Integer/parseInt page) totalCountOfPages)]
    (generate-string {:PreviousPage previousPage
                      :NextPage nextPage
                      :TotalCountOfPages totalCountOfPages
                      :List (retrieve-users (Integer/parseInt page) 5 sortBy descending)})))

;; (set! users (create-users))
;;
;;(grid-builder-data "0" "firstName" true)



