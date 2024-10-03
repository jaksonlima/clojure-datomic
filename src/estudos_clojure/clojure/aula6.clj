(ns test-1.aula6)

(defn imprimir
  [v]
  (class v))

(map imprimir [{:id 1} {:id 2}])

(imprimir {:id 1})

(class {:id 1})

(defn expected-class
  [c]
  (class c)
  )

(map expected-class [1 2.0])

(defn destructure
  [[c v]]
  (println c)
  (println v)
  )

(def pedido {:mochila {:qtd 1 :prc 80} :camiseta {:qtd 1 :prc 80}})

(destructure pedido)

(map destructure pedido)

(defn total-produtos
  [[_ v]]
  (* (:qtd v) (:prc v)))

(map total-produtos pedido)

(->> pedido
    (map total-produtos)
     (reduce +))

(vals pedido)

(defn total-produtos
  [produto]
  (* (:qtd produto) (:prc produto)))

(->> pedido
     vals
     (map total-produtos)
     (reduce +))

(defn gratuito?
  [v]
  (filter #(<= (get :prc %1 0) 0) v))

(gratuito? {:prc 0.1})

(assoc {:id 1} :key "value")

(map #(assoc %1 :key "value") [{} {}])

(update {:id 1} :id inc)
(assoc-in  {:id 1 :list {:name "jack"}} [:list :name ] "lima")


(map inc [1])
(map dec [1 2])

