(ns aplogpa.core
  (:require [clojure.string :as s])
  (:use [clojure.pprint]))

(defn- cleanup
  [text]
  (-> text
      (s/replace #"(\")|( $)|(^\[)" "")))

(defn- to-int
  [numstr]
  (try
    (Integer/parseInt numstr)
    (catch Exception e
      0)))

(defn parse-req
  [r]
  (let [p1 (s/split r #"( - - )|(\+[0-9]{4}\] )")
        quoted (-> p1 last)
        p2 (-> quoted (s/replace #"\"[^\"]+\"" "") (s/split #" "))
        p3 (->> quoted (re-seq #"\"[^\"]+\""))
        request (-> p3 first (s/split #" "))
        server (-> p3 second)
        method (-> request first (subs 1))
        endpoint (-> request second)
        protocol (-> request (nth 2))
        big-list (-> (drop-last p1) (concat p2 p3 [method endpoint protocol server]))
        infos (map cleanup big-list)]
    (assoc {}
      :origin-ip (nth infos 0)
      :date-time (nth infos 1)
      :response-code (to-int (nth infos 3))
      :duration (to-int (nth infos 4))
      :method method
      :endpoint endpoint
      :protocol protocol
      :server server
      :user-agent (nth infos 7))))

(defn- split-lines
  [lines]
  (s/split lines #"\n"))

(defn parse-logfile
  [filename]
  (->> (slurp filename)
       split-lines
       (map parse-req)))
