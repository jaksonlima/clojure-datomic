(ns test-1.loja.aula10
  (:require [test-1.loja.aula9 :refer :all]
            [test-1.loja.db :refer :all]))

(println (details-orders-clients))

(->> (details-orders-clients)
     (sort-by :price-total-buy)
     (reverse))

(->> (details-orders-clients)
     (sort-by :user-id))

(do (println (orders))
(get-in (orders) [0 :items :mochila :price]))

(nth (orders) 0)

(take 2 (orders))

(filter #(< (%1 :price-total-buy) 500) (details-orders-clients))
(some #(> (%1 :price-total-buy) 500) (details-orders-clients))