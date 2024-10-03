(ns estudos-clojure.aula16
  (:require [estudos-clojure.model :as h.model])
  (:use [clojure.pprint]))

(defn chega-em
  [fila pessoa]
  (conj fila pessoa))

(defn chega-em!
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (pprint fila)
    (dosync
      (ref-set fila (chega-em @fila pessoa)))))

(defn chega-em!
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (ref-set fila (chega-em @fila pessoa))))

(defn chega-em!
  [hospital pessoa]
  (let [fila (get hospital :espera)]
    (alter fila chega-em pessoa)))

(defn async-chega-em!
  [hospital pessoa]
  (future
    (Thread/sleep (.longValue (rand 5000)))
    (dosync
      (println "Tentando o codigo sincronizado" pessoa)
      (chega-em! hospital pessoa))))

(defn simula-um-dia
  []
  (let [hospital {:espera (ref h.model/fila-vazia)
                  :lab1   (ref h.model/fila-vazia)
                  :lab2   (ref h.model/fila-vazia)
                  :lab3   (ref h.model/fila-vazia)}]

    (dotimes [person 10]
      (async-chega-em! hospital person))

    (future
      (Thread/sleep 8000)
      (pprint hospital))
    ))

(simula-um-dia)

(future 15)

(println (future ((Thread/sleep 1000) 15)))

(def obj {:status :pending :id (rand-int 100)})
(get obj :status)
(get obj :id)
(get obj :name "wait")