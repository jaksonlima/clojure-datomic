(ns test-1.aula4)

(def vetor [1 2 3])

(get vetor 3 0)
(conj vetor 4)
(inc 5)
(update vetor 0 inc)
(update vetor 0 dec)

(defn soma
  [valor]
  (+ valor 1))

(update vetor 0 soma)

(def preco [1 2 3])

(map soma preco)

(def valores (range 10))

(println valores)

(filter even? valores)

(count valores)

(reduce + 5 valores)

(defn soma-reduce
  [v1 v2]
  (println v1 v2)
  (+ v1 v2))

(reduce soma-reduce valores)

'(1 2 3)
