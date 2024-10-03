(ns test-1.loja.db)

(def order1 {:user 15
              :items {
                      :mochila {:id :mochila :quantity 2 :price 80}
                      :camiseta {:id :camiseta :quantity 3 :price 40}
                      :tenis {:id :tenis :quantity 1 :price 40}
                      }
              })

(def order2 {:user 20
              :items {
                      :mochila {:id :mochila :quantity 2 :price 80}
                      :camiseta {:id :camiseta :quantity 3 :price 40}
                      :tenis {:id :tenis :quantity 1 :price 40}
                      }
              })

(def order3 {:user 20
              :items {
                      :mochila {:id :mochila :quantity 2 :price 80}
                      :camiseta {:id :camiseta :quantity 3 :price 40}
                      :tenis {:id :tenis :quantity 1 :price 40}
                      }
              })

(defn orders [] [order1 order2 order3])