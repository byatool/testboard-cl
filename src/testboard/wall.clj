(ns testboard.wall
  (:use [hiccup core page]
        [cheshire.core :only (generate-string)]
        [testboard.utility :only (resolve-previous-page resolve-next-page to-key)]
        [testboard.clojure-macro :only (--|)]
        [testboard.view :only (master-page)])
   (:require
   [clj-time.core :as time]
   [clj-time.format :as time-format]))


(def subject-items [])
(def current-id 0)


;; Utility

(defn add-to-subject [subject-id id text user-name]
  (def subject-items
    (vec
     (cons {:Id id
            :Text text
            :SubjectId subject-id
            :Date (time/now)
            :Username user-name}
           subject-items))))


(defn create-ui-subject-item [item]
  {:Id (:Id item)
   :SubjectId (:SubjectId item)
   :Text (:Text item)
   :Date (time-format/unparse (time-format/formatter "dd-MM-yyyy HH:mm:ss") (:Date item))
   :Username (:Username item)})


(defn edit-list [id text]
  (map #(if (= (:Id %) id)
          (assoc % :Text text)
          %)
       subject-items))


(defn retrieve-subject-items [page per-page sort descending]
  (->>
   (sort-by (to-key sort)
            (if descending
              #(compare %2 %1)
              #(compare %1 %2))
            subject-items)
   (--| drop (* page 5))
   (--| take per-page)))


;; Get 
(defn wall-page [id]
  (master-page [:div
                [:div {:id "mainContainer"}]
                [:script
                 "var result = src.base.control.wall.initialize('mainWall', '/wallpagepost/', '/wallpagedata/', '/wallpagedelete/', '1','/wallpageedit/');"
                 "document.getElementById('mainContainer').appendChild(result);"]]))


;; Post

(defn wall-page-data [subject-id page]
  (let [total-count-of-pages (/ (count subject-items) 5)
        previous-page        (resolve-previous-page page)
        next-page            (resolve-next-page page total-count-of-pages)]
    (generate-string {:PreviousPage previous-page
                      :NextPage next-page
                      :TotalCountOfPages total-count-of-pages
                      :List (map create-ui-subject-item (retrieve-subject-items page 5 "date" true))})))


(defn wall-page-delete [id]
  (do
    (def subject-items (remove #(= (:Id %) id) subject-items))
    (generate-string
     {:MessageItems [{:Message "Success" :MessageType "info"}]})))


(defn wall-page-edit [item-id text]
  (do
    (def subject-items
      (edit-list item-id text))
    (generate-string
     {:MessageItems [{:Message "Success" :MessageType "info"}]})))


(defn wall-page-post [text subject-id]
  (do
    (def current-id (+ current-id 1))
    (add-to-subject subject-id current-id text "Sean")
    (generate-string 
     {:MessageItems [{:Message "Success" :MessageType "info"}]})))
