(ns estudos-clojure.aula21-test
  (:require [clojure.test :refer :all]
            [estudos-clojure.aula21 :refer :all]))

(deftest test-transferencia
  (testing "transferir correto"
    (is (= {:espera ["2", "3"], :lab1 ["4", "5", "6", "1"]}
           (transfere (novo-hospital) :espera :lab1)))
    )

  )