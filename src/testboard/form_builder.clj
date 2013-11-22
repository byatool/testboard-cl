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
                     "var specs = [ "
                     " {type: 'date', id: 'today', class: 'inputTextbox', label: 'today: ', "
                     "  validation: ["
                     "   ['is not empty', 'Today is required'],"
                     "   ['is a valid date', 'Today is not a proper date' ]"
                     "  ]},"
                     "{type: 'text', id: 'firstName', class: 'inputTextbox', label: 'first name: ', "
                     "  validation: ["
                     "   ['is not empty', 'First name is required']"
                     "  ]},"
                     "{type: 'select', id: 'status', class: 'inputSelect', label: 'status: ', "
                     "  validation: ["
                     "   ['is not empty', 'Status is required'],"
                     "  ],"
                     "  parameters: {id: 1},"
                     "  defaultValue: 'choose', "
                     "  url: '/formbuilderselect/'} "
                     "];"
                     "var result = src.base.control.formBuilder.initialize("
                     " 'formContainer',"
                     " '/formbuilderpost/', "
                     " specs"
                     ");"
                     "document.getElementById('mainContainer').appendChild(result);")]
   (master-page [:div
                 [:div {:id "mainContainer"}]
                 [:pre script-text]
                 [:script
                  script-text]])))



(defn form-builder-post [the-date first-name status]
  (generate-string 
   {:MessageItems
    [{:Message
      (join ["Success in saving " the-date " with " first-name " and status " status])
      :MessageType "info"}]}))


(defn form-builder-select [id]
  (generate-string
   [{:text "a" :value "A"}
    {:text "b" :value "B"}]))
