(ns testboard.editable-div
  (:use
   [clojure.string :only (join replace)]
   [testboard.view :only (master-page)]
   [testboard.utility :only (append-return resolve-previous-page resolve-next-page to-key)]
   [clojure.tools.trace])
  (:require
   [cheshire.core :refer :all]))


(def current-text "this is the text")

(defn editable-div-page []
  (let [script-text
        (append-return
         "var EditableDiv_ = src.base.control.editableDiv; "
         "var holder = document.getElementById('mainContainer'); "
         "var editableDiv = EditableDiv_.initialize( "
         "  'theEditableDiv', "
         "  'this is the text', "
         "  '1', "
         "  '/editabledivresult/'); "
         "holder.appendChild(editableDiv);" )]
   (master-page [:div
                 [:div {:id "mainContainer"}]
                 [:script
                  "var EditableDiv_ = src.base.control.editableDiv; "
                  "var holder = document.getElementById('mainContainer'); "
                  "var editableDiv = EditableDiv_.initialize( "
                  "  'theEditableDiv', "
                  (join ["'" current-text "', "])
                  "  '1', "
                  "  '/editabledivresult/'); "
                  "holder.appendChild(editableDiv);"]])))

(defn editable-div-page-result [text id]
  
  (let [new-text (clojure.string/replace text #"\n" "\r")]
   (do
     (trace new-text)
     (def current-text new-text)
     (generate-string {:MessageItems [{:Message (join [ text id]) :MessageType "error"}]}))))
;; (do
;;   
;;   )
(clojure.string/replace "the text\n" #"\n" "\r")
