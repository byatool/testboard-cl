(ns testboard.view
  (:use [clojure.string :only (join)]
        [hiccup core page]
        [cheshire.core :only (generate-string)]))


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


(defn editable-div-page-result [text id]
  (generate-string {:MessageItems [{:Message (join [text id]) :MessageType "error"}]}))

;;
