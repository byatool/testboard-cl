(ns testboard.view
  (:use [clojure.string :only (join capitalize blank?)]
        [hiccup core page]
        [cheshire.core :only (generate-string)]
        [testboard.utility :only (resolve-previous-page resolve-next-page to-key)]
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
                [:a {:href "/formbuilderpage"} "form buidler "]
                [:br]
                [:a {:href "/popupdatepicker"} "popup date picker "]
                [:br]
                [:a {:href "/gridbuilderpage"} "grid builder "]
                ]))


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



;; Grid Builder Post
