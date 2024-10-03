(ns estudos-clojure.aula13
  (:require [estudos-clojure.model :as h.model])
  (:use [clojure.pprint]))

(defn cabe-na-fila?
  [hospital departamento]
  (-> hospital
      (get departamento)
      (count)
      (< 5)))

(defn chegou-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "File já está cheia" {:person pessoa}))
    ))

(defn atende
  [hospital departamento]
  (update hospital departamento pop))

(defn simula-dia
  []
  (def hospital (h.model/novo-hospital))

  (pprint "inicial >>")
  (pprint hospital)
  (pprint "<<")

  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :espera "111"))
  (def hospital (chegou-em hospital :lab1 "222"))
  (def hospital (chegou-em hospital :lab2 "333"))
  (def hospital (chegou-em hospital :lab3 "444"))

  (def hospital (atende hospital :espera))
  (def hospital (atende hospital :lab3))

  (pprint "fim >>")
  (pprint hospital)
  (pprint "<<")
  )

(simula-dia)


(defn simula-um-dia-paralelo
  []
  (def hospital (h.model/novo-hospital))
  (-> (Thread. #(chegou-em hospital :espera "111"))
      (.start))
  (-> (Thread. #(println "test-2"))
      (.start))
  (-> (Thread. #(println "test-3"))
      (.start))
  (def hospital (chegou-em hospital :espera "222"))
  (pprint hospital)
  )

(simula-um-dia-paralelo)

(do
  (Thread/sleep 10000)
  (pprint "test")
  )

(update-in {:id {:id 1}} [:id :id] inc)
(update {:id 0} :id inc)
(update {:espera []} :espera conj "123" "456")
(update-in {:espera {:data []}} [:espera :data] conj "123" "456")
(update {:espera ["123" "456"]} :espera pop)
(conj [1] 2)
(conj '(1) 2)
(assoc {:id 1} :id 2)
(assoc-in {:id {:data 1}} [:id :data] 3)
(assoc-in {:id {:data [{:name "jack"} {:name "jack-t"}]}} [:id :data 0 :name] "lima")