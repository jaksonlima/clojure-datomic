(ns test-1.loja.aula11
  (:require [test-1.loja.aula9 :refer :all]
            [test-1.loja.db :refer :all]))

(keep #(> (%1 price-total-buy) 500) (details-orders-clients))