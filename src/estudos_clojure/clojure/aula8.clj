(ns test-1.aula8)

(defn my-count
  ([elements] (my-count 0 elements))
  ([init elements]
   (if (seq elements)
     (recur (inc init) (next elements))
     init)))

(my-count (range 10))

(seq [0])
(println (my-count 0 (range 5)))

(reduce + 0 [1 2 3])

(defn my-count-v1
  [elements]
  (loop [count-elements 0
         next-elements elements]
    (if (seq next-elements)
      (recur (inc count-elements) (next next-elements))
      count-elements)))

(my-count-v1 [1 2 3 4])

(rest [1 2 3])








