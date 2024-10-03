(ns estudos-clojure.datomic.aula5
  (:require [datomic.api :as d]
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
                 :db/cardinality :db.cardinality/one}
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

(def Sell {:sell/id                       UUID
           :sell/quantity                 s/Int
           :sell/product                  [Product]
           (s/optional-key? :sell/status) s/Str})

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
  (d/q '[:find (pull ?e [* {:sell/product [* {:product/category [*]}]}])
         :where [?e :sell/id]] (d/db conn)))

(query-all)

(defn instant-las-save-sell []
  (d/q '[:find ?instant .
         :in $ ?id
         :where [_ :sell/id ?id ?tx true]
         [?tx :db/txInstant ?instant]]
       (d/db conn) (UUID/fromString "3ebfb159-7cab-4ef1-b6a3-2dc98f3b0fee")))

;(instant-las-save-sell)

(defn sum-sell-instance-past []
  (let [instant (instant-las-save-sell)]
    (d/q '[:find ?price-with-quantity
           :keys price
           :in $ ?id
           :where [?sell :sell/id ?id]
           [?sell :sell/quantity ?quantity]
           [?sell :sell/product ?product]
           [?product :product/price ?price]
           [(* ?quantity ?price) ?price-with-quantity]]
         (d/as-of (d/db conn) instant) (UUID/fromString "3ebfb159-7cab-4ef1-b6a3-2dc98f3b0fee"))
    )
  )

;(sum-sell-instance-past)

;(d/q '[:find [(pull ?e [*]) ...]
;       :where [?e :product/id]]
;     (d/db conn))

;(d/pull (d/db conn) '[*] [:product/id (UUID/fromString "1f60c691-5bf9-4787-a46d-5fe65419bee9")])

;(UUID/fromString "5d038671-75f4-48f2-968e-d547a3db6e9e")
;(apply + [10M 10M])

;(d/transact conn [(create-product)])

;(d/transact conn
;            [[:db/add [:product/id (UUID/fromString "78f85319-fe7d-4341-8da2-2fe0d0f89cce")]
;              :product/price 33M]])

;(d/transact conn
;            [[:db/add [:sell/id (UUID/fromString "3ebfb159-7cab-4ef1-b6a3-2dc98f3b0fee")]
;              :sell/product [:product/id (UUID/fromString "78f85319-fe7d-4341-8da2-2fe0d0f89cce")]]])

;(d/transact conn
;            [{:sell/id (UUID/fromString "3ebfb159-7cab-4ef1-b6a3-2dc98f3b0fee")
;              :sell/product (create-product) }])

;(d/transact conn [[:db/retractEntity [:sell/id (UUID/fromString "43db00cb-7446-4aae-b734-60ffa92ad990")]]])

(defn query-all-history-true []
  (d/q '[:find ?sell ?id
         :where [?sell :sell/id ?id _ true]]
       (d/history (d/db conn))))

;(query-all-history-true)

(defn query-all-history-false []
  (d/q '[:find ?sell ?id
         :where [?sell :sell/id ?id _ false]]
       (d/history (d/db conn))))

;(query-all-history-false)

;(sort-by first #{[5 6] [3 4] [0 1] [1 2]})
;(first #{[0 1] [1 2]})
;(second [2 3])

(defn query-price-with-history []
  (->> (d/q '[:find ?instance ?price
              :in $ ?id
              :where [?product :product/id ?id]
              [?product :product/price ?price ?tx false]
              [?tx :db/txInstant ?instance]]
            (d/history (d/db conn)) (UUID/fromString "78f85319-fe7d-4341-8da2-2fe0d0f89cce"))
       (sort-by first)))

;(query-price-with-history)

(d/transact conn [{:sell/id     #uuid"3c1109d4-34ca-4e9e-b229-628aeb95f196"
                   :sell/status "open-finally"}])
;
;(d/transact conn [[:db/add [:sell/id #uuid"3c1109d4-34ca-4e9e-b229-628aeb95f196"] :sell/status "close"]])
;
;(d/pull (d/db conn) '[*] [:sell/id #uuid"3c1109d4-34ca-4e9e-b229-628aeb95f196"])

(defn query-wih-since []
  (let [db (d/db conn)
        since (d/since db #inst"2021-10-02T10:54:00.898-00:00")]
    (d/q '[:find ?instant ?status ?id
           :in $ $since
           :where [$ ?sell :sell/id ?id]
           [$since ?sell :sell/status ?status ?tx]
           [$since ?tx :db/txInstant ?instant]]
         db since)))

;(query-wih-since)















