(ns estudos-clojure.aula21
  (:require [schema.core :as s])
  (:use [clojure.pprint]))

(s/set-fn-validation! true)

(def fila-vazia clojure.lang.PersistentQueue/EMPTY)

(defn novo-hospital []
  {:espera      fila-vazia
   :laboratorio fila-vazia})

(def PacienteID s/Str)
(def Departamento (s/queue PacienteID))
(def Hospital {s/Keyword Departamento})

(s/validate PacienteID "111")
;(s/validate PacienteID 98)
(pprint (s/validate Departamento (conj fila-vazia "76" "23")))
(s/validate Hospital {:espera fila-vazia})
;(s/validate Hospital {:espera []})

(s/defn novo-hospital :- Hospital
  []
  {:espera (conj fila-vazia "1" "2" "3")
   :lab1   (conj fila-vazia "4" "5" "6")})

;(defn sum [v]
;  (apply + v))
;(def all (comp sum #(get %1 %2) ))
;(all {:espera [1 2]} :espera)

;(defn add1 [x]
;  (println "add1" x)
;  (+ x 1))
;(defn double [x]
;  (println "double" x)
;  (* x 2))
;(defn square [x]
;  (println "square" x)
;  (* x x))
;(def add1-double-square (comp square double add1))
;(add1-double-square 3)

;(defn equals-input-output
;  [h, o, d, p]
;  (let [e-h-fn #(get %1 %2)
;        c-a #(count %1)
;        all (comp e-h-fn c-a)
;        e-h (e-h-fn h d)
;        e-p (e-h-fn h p)]
;    )
;  )

(s/defn transfere :- Hospital
  [hospital :- Hospital, de :- s/Keyword, para :- s/Keyword]
  {
   :pre [(contains? hospital de), (contains? hospital para), (some? hospital)]
   }
  (let [pessoa-peek (peek (de hospital))
        pessoa-pop (pop (de hospital))]
    (-> hospital
        (assoc de pessoa-pop)
        (update para conj pessoa-peek))))

(pprint
  (-> (novo-hospital)
      (transfere :espera :lab1)
      (transfere :espera :lab1)
      (transfere :espera :lab1)))

(= {:espera [], :lab1 ["4", "5", "6", "1", "2"]}
   (-> (novo-hospital)
       (transfere :espera :lab1)
       (transfere :espera :lab1)
       (transfere :espera :lab1)))






