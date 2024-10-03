(ns test-1.aula5)

(def mochila {"mochila" 1, "caneta" 5})

(println mochila)
(keys mochila)
(vals mochila)

(def estoque {:mochila 1 :caneta 6})

(println estoque)

(assoc estoque :lapis 2)
(assoc estoque :mochila 2)
(update estoque :mochila inc)

(defn remove
  [val]
  (println val)
  (- val 1))

(update estoque :mochila #(+ % 1))

(dissoc estoque :mochila)

(def pedido {
             :mochila {:quantidade 2 :preco 80}
             :camiseta {:quantidade 3 :preco 40}
             })

(println pedido)

(assoc pedido :chaveiro {:quantidade 3 :preco 20})

(pedido :mochila)
(get pedido :mochila)
(get pedido :mochila- {})

(:mochila pedido)

(update-in pedido [:mochila :quantidade] dec)

(-> pedido
    :mochila
    :quantidade)

(def
  clientes {
            :15 {
                 :nome "Guilherme"
                 :certificados ["Clojure" "Java" "Machine Learning"] }
            :16 {
                 :nome "Guilherme"
                 :certificados ["Clojure" "Java" "Machine Learning"] } })

(:15 clientes)
(count clientes)

(-> clientes
    :15
    :certificados
    count)


