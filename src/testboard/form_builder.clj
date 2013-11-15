(ns testboard.form-builder
  (:use [hiccup core page]
        [cheshire.core :only (generate-string)]
        [testboard.utility :only (previous-page next-page to-key)]
        [testboard.clojure-macro :only (--|)]
        faker.name
        faker.internet)
  (:require
   [clj-time.core :as time]
   [clj-time.format :as time-format]))
