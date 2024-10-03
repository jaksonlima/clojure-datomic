(ns test-1.loja.aula9
  (:require [test-1.loja.db :refer :all]))

(group-by :user (orders))

(defn details-order-client
  [[id items]]
  {:user-id id
   :total-items (count items)
   :price-total-buy (->> (map :items items)
               (map vals)
               (flatten)
               (reduce #(+ %1 (* (get %2 :quantity 0) (get %2 :price 0))) 0))})

(->> (orders)
     (group-by :user)
     (map details-order-client))

(defn details-orders-clients
  []
  (->> (orders)
       (group-by :user)
       (map details-order-client)))

(take 5 (orders))

(defn total-order-items
  [items]
  (reduce #(+ %1 (* (get %2 :quantity 0) (get %2 :price 0))) 0 items))

(defn total-order-items
  [inc item]
  (+ inc (* (get item :quantity 0) (get item :price 0))))

(defn total-order-items
  [inc item]
  (println item)
  (let [quantity (get item :quantity 0)
        price (get item :price 0)
        total (* quantity price)]
    (+ inc total)))

(defn details-order-client
  [[id items]]
  {:user-id id
   :total-items (count items)
   :price-total-buy (->> (mapcat :items items)
                         (flatten)
                         (reduce total-order-items 0))})

(->> (orders)
     (group-by :user)
     (map details-order-client))

(defn test
  [[id items]]
  (println "->>")
  (->> (mapcat :items items)
       (flatten)))

(defn test
  [[id items]]
  (println "->>")
  (->> (map :items items)
       (map vals)
       (flatten)))

(->> (orders)
     (group-by :user)
     (map test))

(take 2 (range 1000))

(defn printed
  [v]
  (println "printed ->" v)
  v)

(->> (range 50)
     (map printed)
     (map printed)
     )