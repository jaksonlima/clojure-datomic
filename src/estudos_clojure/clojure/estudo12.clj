(ns estudos-clojure.estudo12
  (:use clojure.pprint))

(defn fila
  []
  (let [espera [1 2 3]]
    (println espera)
    (println (conj espera 4))
    ;(println (disj espera 1))
    (println (pop espera))
    ))

(fila)
(conj '(1 2) 5)
(conj #{1} 2)

(defn fila-v1
  []
  (let [fila (conj clojure.lang.PersistentQueue/EMPTY "1" "2" "3")]
    (println (seq fila))
    (pprint fila)
    (println (seq (conj fila "4")))
    (println (seq (pop fila)))
    (println (peek fila))
    ))

(fila-v1)







