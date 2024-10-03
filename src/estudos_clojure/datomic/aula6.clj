(ns estudos-clojure.datomic.aula6
  (:require [clojure.test.check.generators :as gen]
            [datomic.api :as d]
            [schema-generators.generators :as g]
            [schema.core :as s])
  (:use [clojure.pprint])
  (:import (java.util UUID)))

(def db-uri "datomic:dev://localhost:4334/hello")
(def create-db (d/create-database db-uri))
(def conn (d/connect db-uri))

(defn delete-db [uri]
  (d/delete-database uri))

(pprint create-db)
(pprint conn)
;(pprint (delete-db db-uri))

(defn create-schema [conn]
  (let [schema [; Product
                {:db/ident       :product/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :product/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/slug
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/price
                 :db/valueType   :db.type/bigdec
                 :db/cardinality :db.cardinality/one
                 :db/index       true}
                {:db/ident       :product/key
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/many}
                {:db/ident       :product/category
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/stock
                 :db/valueType   :db.type/long
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/digital
                 :db/valueType   :db.type/boolean
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/variation
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/many
                 :db/isComponent true}
                {:db/ident       :product/visualization
                 :db/valueType   :db.type/long
                 :db/cardinality :db.cardinality/one
                 :db/noHistory   true}

                ;Variation Product
                {:db/ident       :variation/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :variation/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :variation/price
                 :db/valueType   :db.type/bigdec
                 :db/cardinality :db.cardinality/one}

                ;Sell
                {:db/ident       :sell/id
                 :db/valueType   :db.type/uuid
                 :db/unique      :db.unique/identity
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :sell/quantity
                 :db/valueType   :db.type/long
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :sell/product
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/many}
                {:db/ident       :sell/status
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}

                ; Category
                {:db/ident       :category/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :category/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                ]]
    (d/transact conn schema)))

;(create-schema conn)

(s/set-fn-validation! true)

(def Category {:category/name s/Str
               :category/id   UUID})

(def Variation {:variation/id    UUID
                :variation/name  s/Str
                :variation/price BigDecimal})

(def Product {:product/id                             UUID
              :product/name                           s/Str
              :product/slug                           s/Str
              :product/price                          BigDecimal
              :product/key                            [s/Str]
              :product/digital                        s/Bool
              (s/optional-key :product/stock)         Long
              (s/optional-key :product/category)      Category
              (s/optional-key :product/variation)     [Variation]
              (s/optional-key :product/visualization) Long})

(def Sell {:sell/id                      UUID
           :sell/quantity                s/Int
           :sell/product                 [Product]
           (s/optional-key :sell/status) s/Str})

(s/defn create-category :- Category
  []
  {:category/id   (UUID/randomUUID)
   :category/name "mac"})

(s/defn create-variation :- Variation
  []
  {:variation/id    (UUID/randomUUID)
   :variation/name  "silicon"
   :variation/price 50M})

(s/defn create-product :- Product
  []
  {:product/id            (UUID/randomUUID)
   :product/name          "mac"
   :product/slug          "/mac"
   :product/price         51M
   :product/key           ["2023" "2080"]
   :product/digital       false
   :product/stock         10
   :product/category      (create-category)
   :product/variation     [(create-variation)]
   :product/visualization 0})

(s/defn create-sell :- Sell
  []
  {:sell/id       (UUID/randomUUID)
   :sell/quantity 5
   :sell/product  [(create-product)]
   :sell/status   "any"})

(defn create [& data]
  (d/transact conn (vec data)))

;(create (create-sell))

(defn query-all []
  (d/q '[:find (pull ?e [*])
         :where [?e :sell/id]]
       (d/db conn)))

;(query-all)

(defn double-to-big-decimal [v] (BigDecimal/valueOf ^double v))
(def big-decimal-gen (gen/fmap double-to-big-decimal (gen/double* {:infinite? false, :NaN? false})))
(def instance-big-decimal {BigDecimal big-decimal-gen})

(defn g-sample
  ([v] (g-sample 5 v))
  ([q v] (g/sample q v instance-big-decimal)))

;(g-sample 20 Category)
;(g-sample 10 Variation)
;(g-sample 5 Product)
;(g-sample Product)
;(g-sample Sell)

;(def products (g-sample 200 Product))
;(d/transact conn products)

(defn generator-products []
  (dotimes [_ 50]
    (let [products (g-sample 200 Product)]
      (d/transact conn products))))

;(time (generator-products))

(defn query-all-products []
  (d/q '[:find ?price
         :where [?e :product/price ?price]]
       (d/db conn)))

;(time (count (query-all-products)))

;(= "j" "j")

(defn query-all-products []
  (d/q '[:find ?name
         :in $ ?price-q ?name-q
         :where [?e :product/price ?price-q]
                [?e :product/name ?name]
                [(.contains ?name-q ?name)]]
       (d/db conn) 100M "j"))

;(time (count (query-all-products)))

(defn propriedades-do-valor [valor]
  (if (vector? valor)
    (merge {:db/cardinality :db.cardinality/many}
           (propriedades-do-valor (first valor)))
    (cond (= valor java.util.UUID) {:db/valueType :db.type/uuid
                                    :db/unique    :db.unique/identity}
          (= valor s/Str) {:db/valueType :db.type/string}
          (= valor BigDecimal) {:db/valueType :db.type/bigdec}
          (= valor Long) {:db/valueType :db.type/long}
          (= valor s/Bool) {:db/valueType :db.type/boolean}
          (map? valor) {:db/valueType :db.type/ref}
          :else {:db/valueType (str "desconhecido: " (type valor) valor)})))

(defn extrai-nome-da-chave [chave]
  (cond (keyword? chave) chave
        (instance? schema.core.OptionalKey chave) (get chave :k)
        :else key))

(defn chave-valor-para-definicao [[chave valor]]
  (let [base {:db/ident       (extrai-nome-da-chave chave)
              :db/cardinality :db.cardinality/one}
        extra (propriedades-do-valor valor)
        schema-do-datomic (merge base extra)]
    schema-do-datomic))

(defn schema-to-datomic [definicao]
  (mapv chave-valor-para-definicao definicao))

;(pprint (schema-to-datomic Category))
;(pprint (schema-to-datomic Variation))
;(pprint (schema-to-datomic Sell))
;(pprint (schema-to-datomic Product))









