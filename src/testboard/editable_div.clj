(ns testboard.editable-div
  (:use
   [clojure.string :only [join]]
   [testboard.view :only (master-page)]
   [testboard.utility :only (append-return resolve-previous-page resolve-next-page to-key)])
  (:require
   [cheshire.core :refer :all]))


(def current-text "this is the text\nthere\n")

(defn editable-div-page []
  (let [script-text
        (append-return
         "src.base.helper.domHelper.submitToUrl('/editable_div_start_text/', {}, "
         "  function(result) { "
         "    var EditableDiv_ = src.base.control.editableDiv; "
         "    var holder = document.getElementById('mainContainer'); "
         "    var editableDiv = EditableDiv_.initialize( "
         "      'theEditableDiv', "
         "      result,"
         "      '1', "
         "      '/editabledivresult/'); "
         "    holder.appendChild(editableDiv);"
         "  });" )]
   (master-page [:div
                 [:div {:id "mainContainer"}]
                 [:script
                  script-text]])))


(defn editable-div-start-text []
  (generate-string current-text))

(defn editable-div-page-result [text id]
  (let [new-text text]
    (do
      (def current-text new-text)
      (generate-string {:MessageItems [{:Message (join [text id]) :MessageType "error"}]}))))




