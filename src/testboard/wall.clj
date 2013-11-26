(ns testboard.wall
 (:use [hiccup core page]
       [cheshire.core :only (generate-string)]
       [testboard.utility :only (resolve-previous-page resolve-next-page to-key)]
       [testboard.clojure-macro :only (--|)]
       [testboard.view :only (master-page)]
       [testboard.utility :only (append-return)])
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
  
  
  (let [script-text (append-return
                     "var result = src.base.control.wall.initialize("
                     " document, "
                     " 'mainWall', "
                     " '/wallpagepost/', "
                     " '/wallpagedata/',"
                     " '/wallpagedelete/', "
                     " '1',"
                     " '/wallpageedit/'"
                     ");"
                     " "
                     "document.getElementById('mainContainer').appendChild(result);")]
    (master-page [:div
                  [:div
                   {:id "mainContainer"}]
                  [:pre
                   script-text]
                  [:pre
                   (append-return
                    " "
                    "ABOUT"
                    " "
                    "(containerId, postTo, retrieveItemsUrl, deleteUrl, subjectId, editableUrl)"
                    " "
                    " postTo:"
                    "  The url the post information is sent to."
                    "    entryTextbox  The wall entry textbox name"
                    "    subjectId     The id of the wall it will be added to."
                    "      This assumes there is table that links a subjectId to"
                    "       many wall posts."
                    " "
                    " retrieveItemsUrl:"
                    "  The url used to get a list of existing wall posts."
                    "    subjectId   The id of the wall the posts are assigned to."
                    "    page        The current page number.  This will be sent automatically"
                    "                  with the paging control, and is 0 by default."
                    " "
                    "  required result:"
                    "    list"
                    "      PreviousPage"
                    "        number"
                    "        This is the page number of the new previous page"
                    "      NextPage"
                    "        number"
                    "        This is the page number of the new next page"
                    "      TotalCountOfPages"
                    "        number"
                    "        The count of how many pages (Items/Per Page) there are."
                    "      List"
                    "        {Id       :number"
                    "         Subject  :string"
                    "         Text     :string"
                    "         Date     :string"
                    "         Username :string}"
                    " "
                    " deleteUrl:"
                    "  The url used to delete an item."
                    "   id   The id of the post.  This is automatically set/sent by the wall."
                    "  "
                    "  required result:"
                    "    {:MessageItems [{:Message 'Success' :MessageType 'info'}]}"
                    "   or"
                    "    {:MessageItems [{:Message 'Failed' :MessageType 'error'}]}"
                    "  The message will be display automatically by the control.  The 'Success' and"
                    "   'Failed' messages are specific text.  Anything can be sent back for the text"
                    "   message."
                    " "
                    " subjectId:"
                    "   The id of the wall the posts are assigned to.  Should be used to retrieve posts."
                    " " 
                    " editableUrl:"
                    "   This is used when an item is edited by clicking on the text, and submitting the"
                    "    new text."
                    "   text        :  The new text."
                    "   itemId      :  The id of the post to edit."
                    " "
                    "  required result:"
                    "    {:MessageItems [{:Message 'Success' :MessageType 'info'}]}"
                    "   or"
                    "    {:MessageItems [{:Message 'Failed' :MessageType 'error'}]}"
                    "  The message will be display automatically by the control."
                    
                    )]
                  [:script
                   script-text]])))


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
