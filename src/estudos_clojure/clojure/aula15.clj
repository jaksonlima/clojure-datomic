(ns estudos-clojure.aula15
  (:require [estudos-clojure.model :as h.model])
  (:use [clojure.pprint]))

(defn chega-em
  [hospital pessoa]
  (update hospital :espera conj pessoa))

(defn chega-em!
  [hospital pessoa]
  (swap! hospital chega-em pessoa))

(defn transferir
  [hospital de para]
  (let [fila (get @hospital de)
        peek-pop (juxt peek pop)
        [person new-queue] (peek-pop fila)]
    (swap! hospital update para conj person)
    (swap! hospital assoc de new-queue)))

(defn simula-dia
  []
  (let [hospital (atom (h.model/novo-hospital))]

    (doseq [person ["jack" "lima" "wilson"]]
      (chega-em! hospital person))

    (doseq [transfer [:espera :espera]
            location [:lab1]]
      (transferir hospital transfer location))

    (doseq [transfer [:lab1]
            location [:lab2]]
      (transferir hospital transfer location))

    (doseq [transfer [:lab2]
            location [:lab3]]
      (transferir hospital transfer location))

    (pprint @hospital)
    ))

(simula-dia)

(defn add1 [x]
  (+ x 1))

(defn double [x]
  (* x 2))

(defn square [x]
  (* x x))

(def composed-fn (comp add1 double square))

(composed-fn 2)

(def juxt-fn (juxt add1 double square))

(juxt-fn 2)

(def my-atom (atom {:id 1 :name "lima" :details [{:description "atom"}]}))
(println @my-atom)
(swap! my-atom assoc :id 2)
(swap! my-atom update :id inc)
(swap! my-atom assoc-in [:details 0 :description] "my atom")