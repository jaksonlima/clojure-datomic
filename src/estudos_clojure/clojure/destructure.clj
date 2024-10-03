(ns estudos-clojure.destructure)

(defn destructure
  [{:keys [age last-name]
    name :name
    [one two three] :data
    {:keys [street number]} :address}]
  (println one two three)
  (println name)
  (println age last-name)
  (println street number))

(destructure {:name "jack"
              :last-name "lima"
              :age 27
              :data [1 2 34]
              :address {:street "rua street" :number "number is"}})