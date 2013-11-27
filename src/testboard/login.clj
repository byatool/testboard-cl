(ns testboard.login
  (:use
   [hiccup core page]
   [cheshire.core :only (generate-string)]
   [testboard.utility :only (append-return)]
   [testboard.view :only (master-page)]))



(defn login-page []
  (let [script-text (append-return
                     "var login = "
                     "  src.base.control.login.initialize("
                     "    'loginContainer',"
                     "    '/loginpagepost/'"
                     "); "
                     "document.getElementById('mainContainer').appendChild(login);")]
    (master-page [:div
                  [:div {:id "mainContainer"}]
                  [:script
                   script-text]])))

(defn login-page-post [username password]
  (generate-string 
   {:MessageItems []
    :RedirectUrl "/"}))
