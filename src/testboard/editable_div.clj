(ns testboard.editable-div
  (:use
   [clojure.string :only (join)]
   [testboard.view :only (master-page)])
  (:require
   [cheshire.core :refer :all]))


(defn editable-div-page []
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var EditableDiv_ = src.base.control.editableDiv; "
                 "var holder = document.getElementById('mainContainer'); "
                 "var editableDiv = EditableDiv_.initialize( "
                 "  'theEditableDiv', "
                 "  'this is the text', "
                 "  '1', "
                 "  '/editabledivresult/'); "
                 "holder.appendChild(editableDiv);"]]))

(defn editable-div-page-result [text id]
  (generate-string {:MessageItems [{:Message (join [text id]) :MessageType "error"}]}))
