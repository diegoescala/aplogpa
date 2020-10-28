(ns aplogpa.core-test
  (:require [clojure.test :refer :all]
            [aplogpa.core :refer :all]))

(deftest test-parse-req
  (testing "request parsing"
    (let [req "70.19.148.84 - - [26/Oct/2020:22:29:10 +0000] \"GET /assets/css/style.css HTTP/1.1\" 200 5098 \"http://52.4.162.24/\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36\""
          parsed (parse-req req)
          {:keys [response-code origin-ip endpoint method]} parsed]
     (are [x y] (= x y)
       origin-ip "70.19.148.84"
       response-code 200
       method "GET"
       endpoint "/assets/css/style.css"))))
