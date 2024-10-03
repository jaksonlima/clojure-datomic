(ns estudos-clojure.aula14
  (:require [estudos-clojure.model :as h.model])
  (:use [clojure.pprint]))

(h.model/novo-hospital)

(defn test-atom
  []
  (let [hospital (atom (h.model/novo-hospital))]
    ;(pprint hospital)
    ;(pprint (deref hospital))
    ;(pprint @hospital)
    ;(assoc @hospital :lab1 h.model/fila-vazia)
    ;(update @hospital :lab4 conj "111")

    (swap! hospital assoc :lab4 h.model/fila-vazia)

    (swap! hospital update :lab4 conj "111")

    (pprint @hospital)
    ))

(test-atom)

(defn chegou-sleep!
  [hospital pessoa]
  (Thread/sleep (.longValue (rand 1000)))
  (update hospital :espera conj pessoa)
  )

(defn chegou!
  [hospital pessoa]
  (println "adicionar pessoa" pessoa "\n")
  (swap! hospital chegou-sleep! pessoa)
  (println "adicionado pessoa" pessoa "\n")
  )

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. #(chegou! hospital "111")))
    (.start (Thread. #(chegou! hospital "222")))
    (.start (Thread. #(chegou! hospital "333")))
    (.start (Thread. #(chegou! hospital "444")))
    (.start (Thread. #(chegou! hospital "555")))
    (.start (Thread. #(chegou! hospital "666")))
    (Thread/sleep 6000)
    (pprint @hospital)
    )
  )

(defn simula-um-dia-em-paralelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (dotimes [p 6]
      (.start (Thread. #(chegou! hospital p))))
    (Thread/sleep 6000)
    (pprint @hospital)
    )
  )

(simula-um-dia-em-paralelo)

(defn printed [arg1 arg2] (println arg1 arg2))

(printed "test" "1")
(class (partial printed ""))
((partial printed "partial") "01")

(defn printed-partial [arg1] ((partial printed "partial ->") arg1))

(printed-partial "test de parcial")

(doseq [x [1 2 3]
        y [4 5 6]]
  (print x "\n")
  (print y "\n")
  )

(dotimes [x 5]
  (println x))