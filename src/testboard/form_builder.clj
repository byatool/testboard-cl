(ns testboard.form-builder
  (:use
   [hiccup core page]
   [clojure.string :only (join)]
   [cheshire.core :only (generate-string)]
   [testboard.clojure-macro :only (--|)]
   [testboard.utility :only (append-return)]
   [testboard.view :only (master-page)]))



(defn form-builder-page []
  (let [script-text (append-return
                     [
                      "var specs = [ "
                      " {type: 'text', id: 'today', class: 'inputTextbox', label: 'today: ', isDate: true, "
                      "  validation: ["
                      "   ['is not empty', 'Today is required'],"
                      "   ['is a valid date', 'Today is not a proper date' ]"
                      "  ]},"
                      "{type: 'text', id: 'firstName', class: 'inputTextbox', label: 'first name: ', "
                      "  validation: ["
                      "   ['is not empty', 'First name is required']"
                      "  ]}"
                      "];"
                      "var result = src.base.control.formBuilder.initialize("
                      " 'formContainer',"
                      " '/formbuilderpost/', "
                      " specs"
                      ");"
                      "document.getElementById('mainContainer').appendChild(result);"])]
   (master-page [:div
                 [:div {:id "mainContainer"}]
                 [:pre script-text]
                 [:script
                  script-text]])))



(defn form-builder-post [username first-name]
  (generate-string 
   {:MessageItems [{:Message (join [username first-name]) :MessageType "info"}]}))
