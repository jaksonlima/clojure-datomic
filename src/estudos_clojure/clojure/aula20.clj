(ns estudos-clojure.aula20
  (:require [schema.core :as s])
  (:use [clojure.pprint]))

(defn cabe-na-fila?
  [hospital departamento]
  (some-> hospital
      departamento
      count
      (< 5)))

(not (cabe-na-fila? {:espera [1 2 3 4 5]} :normal))

(let []
  (defn increment-id
    [atom-to-data, keyword, fn]
    (println atom)
    (println keyword)
    (println fn)
    (update atom-to-data keyword fn))
  (def my-atom (atom {:id 0}))
  (swap! my-atom increment-id :id #(+ % 10))
  (swap! my-atom update :id inc)
  (println @my-atom)
  (update {:id 0} :id inc)
  (inc 1)
  )

(get {:espera [1]} :normal)
(when-let [fila (get {:espera [1]} :normal)]
  (println fila))

(defn chega-em
  [hospital, departamento, paciente]
  (update hospital departamento conj paciente))

(chega-em {:espera [1]}, :espera, 2)

(try
  (println "access....")
  ;(throw (IllegalAccessException. "error process message"))
  (throw (ex-info "error process message" {:type :process-error}))
  (catch clojure.lang.ExceptionInfo e
    (println (.getMessage e))
    (println (= :process-error (:type (.getData e))))
    ))

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
  {:espera fila-vazia})

(pprint (pop (conj (:espera (novo-hospital)) 1 2 3)))
(pprint (peek (conj (:espera (novo-hospital)) 1 2 3)))
