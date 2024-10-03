(ns estudos-clojure.aula20-test
  (:require [clojure.test :refer :all]
            [estudos-clojure.aula20 :refer :all]))

(deftest cabe-na-fila-testing?
  (testing "cabe na fila ?"
    (is (cabe-na-fila? {:espera []} :espera)))

  (testing "que não sabe na fila está cheia"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]}, :espera))))

  (testing "que não na fila"
    (is (not (cabe-na-fila? {:espera [1 2 3 4 5]} :espera))))

  (testing "que cabe na fila"
    (is (cabe-na-fila? {:espera [1, 2, 3, 4]} :espera)))

  (testing "outro setor, retorna nil setor ':normal' não existe"
    (is (not (cabe-na-fila? {:espera [1]} :normal))))
  )

(deftest chega-em-test
  (testing "testa chega em"
    (is (= {:espera [1, 2]} (chega-em {:espera [1]}, :espera, 2)))
    (is (= {:espera [1, 2, 3, 7]} (chega-em {:espera [1, 2, 3]}, :espera 7)))
    (is (not (= {:espera [1, 2, 3, 7]} (chega-em {:espera [1, 2, 3]}, :espera 8))))
    )
  )