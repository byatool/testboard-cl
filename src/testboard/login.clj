(defn login-page []
  (let [script-text ""]
    (master-page [:div
                  [:div {:id "mainContainer"}]
                  [:script
                   script-text]])))
