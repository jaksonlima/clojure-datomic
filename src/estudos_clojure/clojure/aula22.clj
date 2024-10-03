(ns estudos-clojure.aula22
  (:require [clojure.test.check.generators :as gen]))

(gen/sample gen/boolean 5)
(gen/sample gen/keyword 5)
(gen/sample gen/keyword-ns 5)
(gen/sample gen/any 5)
(gen/sample gen/string)
(gen/sample gen/string-alphanumeric)
(gen/sample gen/char-alphanumeric)
(gen/sample gen/int)
(gen/sample gen/large-integer)
(gen/sample gen/double)
(gen/sample (gen/vector gen/large-integer, 5) 5)

(doseq [filas (gen/sample (gen/vector gen/string-alphanumeric 5) 5)]
  (println filas)
  )

(defn my-print [v]
  (println "chegou" v)
  v)
(some->[]
       my-print)

(empty? [1])

(seq [])
(some #(> 5 %) [ 1 2 3])
(:id {:id 0})
(:idd {:id 0})
(select-keys {:a 1 :b 2} [:c])
(get {:a 1 :b 2} :c)
(select-keys {:a 1 :b 2 :c {:d 0}} [:c :d])
(->{:a 1 :b 2 :c {:d 0}})

(and nil nil)
(= nil 0)
(= "um@gmail.com" "um@gmail.com" "dois@gmail.com")
(contains? [0] 0)
(some #() #{"19/09"})

{(keyword "name") "jack"}
(boolean nil)
(boolean "")
(boolean 0)
(boolean -1)
(boolean {})
(boolean {:id nil})

(def lazy-val (delay (println "Calculando valor...") (+ 1 2)))
(force lazy-val)
(println @lazy-val)

(some-> {:id 0}
        (println "test"))

(some-> nil
        (println "test"))

(as-> nil props
      (println "imprimir...")
      (println "imprimir 2..."))


(defn find-person [people id]
  (some #(when (= (:id %) id) %) people))

; Usando `when-some` para checar se um valor foi encontrado
(let [people [{:id 1 :name "Alice"}
              {:id 2 :name "Bob"}]]
  (when-some [person (find-person people 2)] ; Tenta encontrar a pessoa com `id 2`
    (println "Found person:" (:name person))))