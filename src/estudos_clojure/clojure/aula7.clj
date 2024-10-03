(ns test-1.aula7)

(get [1 2 3] 4 0)
(first [1 2])
(last [1 2])

#{1 1}

(def vetor ["jack" "lima"])
(next vetor)
(rest vetor)
(first vetor)
(last vetor)

(defn my-map
  [fn seq]
  (let [first (first seq)
        rest (rest seq)]
    (fn first)
    (my-map fn rest)))

(my-map println ["jack" "lima"])
(rest [])
(first [])

(defn my-map
  [fn seq]
  (let [first (first seq)]
    (if (not (nil? first))
      (do
        (fn first)
        (my-map fn (rest seq))))))

(my-map println ["jack" "lima"])

(some? false)
(nil? false)

(defn my-map
  [fn seq]
  (let [first (first seq)]
    (if (some? first)
      (do
        (fn first)
        (my-map fn (rest seq))))))

(my-map println ["jack" "lima"])
;;(my-map println (range 10000))

(defn my-map
  [fn seq]
  (let [first (first seq)]
    (if (some? first)
      (do
        (fn first)
        (recur (my-map fn (rest seq)))))))

(my-map println (range 10000))

(recur (range 10))






















