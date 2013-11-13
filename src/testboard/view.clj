(ns testboard.view
  (:use [clojure.string :only (join capitalize blank?)]
        [hiccup core page]
        [cheshire.core :only (generate-string)]
        [testboard.utility :only (previous-page next-page to-key)]
        faker.name
        faker.internet)
  (:require
   [clj-time.core :as time]
   [clj-time.format :as time-format]))


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
                [:a {:href "/popupdatepicker"} "popup date picker "]
                [:br]
                [:a {:href "/gridbuilderpage"} "grid builder "]
                ]))


(defn editable-div-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var EditableDiv_ = src.base.control.editableDiv; "
                 "var holder = document.getElementById('mainContainer'); "
                 "var editableDiv = EditableDiv_.initialize('theEditableDiv', 'this is the text', '1', '/editabledivresult/');"
                 "holder.appendChild(editableDiv);"]]))


(defn format-text-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var FormatTextAreaDisplay_ = src.base.control.formatTextAreaDisplay; "
                 "var holder = document.getElementById('mainContainer'); "
                 "var formatText = FormatTextAreaDisplay_.initialize(document);"
                 "holder.appendChild(formatText);"]]))


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
                 "document.getElementById('mainContainer').appendChild(result[src.base.control.controlConstant.CreatedControl]);" ]]))


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


(defn wall-page [id]
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var result = src.base.control.wall.initialize('mainWall', '/wallpagepost/', '/wallpagedata/', '1','/wallpageedit/');"
                 "document.getElementById('mainContainer').appendChild(result);"]]))


;; Editable Div Post

(defn editable-div-page-result [text id]
  (generate-string {:MessageItems [{:Message (join [text id]) :MessageType "error"}]}))


;; Wall Post
;;(nth subject-items 1)
;;(assoc subject-items {:Id 1, :Text "ad", :SubjectId 1, :Date #<DateTime 2013-11-13T17:11:17.964Z>, :Username "sean"} )

;;(assoc (first subject-items) :Username "test")
;;




(def subject-items [])
(def current-id 0)

(defn create-ui-subject-item [item]
  {:Id (:Id item)
   :SubjectId (:SubjectId item)
   :Text (:Text item)
   :Date (time-format/unparse (time-format/formatter "dd-MM-yyyy HH:mm:ss") (:Date item))
   :Username (:Username item)})

(defn add-to-subject [subject-id id text user-name]
  (def subject-items
    (vec
     (cons {:Id id
            :Text text
            :SubjectId subject-id
            :Date (time/now)
            :Username user-name}
           subject-items))))


(defn retrieve-subject-items [page per-page sort descending]
  (->
   (sort-by (to-key sort)
            (if descending
              #(compare %2 %1)
              #(compare %1 %2))
            subject-items)
   ((partial drop (* page 5)))
   ((partial take per-page))))

(defn wall-page-post [text subject-id]
  (do
    (def current-id (+ current-id 1))
    (add-to-subject subject-id current-id text "Sean")
    (generate-string 
     {:MessageItems [{:Message "Success" :MessageType "info"}]})))

(defn wall-page-data [subject-id page]
  (let [totalCountOfPages (/ (count subject-items) 5)
        previousPage (previous-page page)
        nextPage (next-page page totalCountOfPages)]
    (generate-string {:PreviousPage previousPage
                      :NextPage nextPage
                      :TotalCountOfPages totalCountOfPages
                      :List (map create-ui-subject-item (retrieve-subject-items page 5 "date" true))})))


(defn edit-list [id text]
  (map #(if (= (:Id %) id)
          (assoc % :Text text)
          %)
       subject-items))


(defn wall-page-edit [item-id text]
  (do
    (def subject-items
      (edit-list item-id text))
    (generate-string
     {:MessageItems [{:Message "Success" :MessageType "info"}]})))


;; Grid Builder Post

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

;; Grid Builder Post
(defn grid-builder-data [page sortBy descending]
  (let [totalCountOfPages (/ (count users) 5)
        previousPage (previous-page (Integer/parseInt page))
        nextPage (next-page (Integer/parseInt page) totalCountOfPages)]
    (generate-string {:PreviousPage previousPage
                      :NextPage nextPage
                      :TotalCountOfPages totalCountOfPages
                      :List (retrieve-users (Integer/parseInt page) 5 sortBy descending)})))
