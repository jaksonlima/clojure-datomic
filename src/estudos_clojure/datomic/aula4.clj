(ns estudos-clojure.datomic.aula4
  (:require [datomic.api :as d]
            [schema.core :as s])
  (:use [clojure.pprint])
  (:import (java.util UUID)))

(def db-uri "datomic:dev://localhost:4334/hello")

(defn create-connection-db [uri]
  (d/create-database uri)
  (d/connect uri))

(defn delete-db [uri]
  (d/delete-database uri))

;(def conn (create-connection-db db-uri))
;(pprint conn)
;(pprint (delete-db db-uri))

(defn create-schema [conn]
  (let [schema [; Product
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
                {:db/ident       :product/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :product/category
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/stock
                 :db/valueType   :db.type/long
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

(def Product {:product/id                        UUID
              :product/name                      s/Str
              :product/slug                      s/Str
              :product/price                     BigDecimal
              :product/key                       [s/Str]
              (s/optional-key :product/stock)    [Long]
              (s/optional-key :product/category) Category})

(defn create-category []
  {:category/name "chip"
   :category/id   (UUID/randomUUID)})

(defn create-product []
  {:product/id       (UUID/randomUUID)
   :product/name     "intel"
   :product/slug     "/intel"
   :product/price    1000M
   :product/key      ["045" "007"]
   :product/stock    0
   :product/category (create-category)})

(defn create-all [data conn]
  (d/transact conn data))

;(create-all [(create-product)] conn)

(defn find-category-product-id [conn]
  (d/q '[:find [(pull ?product [{:product/category [*]}]) ...]
         :in $ ?id
         :where [?product :product/id ?id]]
       conn (UUID/fromString "813826a3-d8b4-4015-8d8c-2e91fe429182")))

;(find-category-product-id (d/db conn))

(defn find-more-price-product
  [db-conn]
  (d/q '[:find (pull ?product [*])
         :where [(q '[:find (max ?price)
                      :where [_ :product/price ?price]] $) [[?price]]]
         [?product :product/price ?price]] db-conn))

;(find-more-price-product (d/db conn))

(defn find-spec-one-register
  [db-conn]
  (d/q '[:find ?price
         :keys price
         :where [?product :product/id]
         [?product :product/price ?price]
         [(>= ?price 900)]] db-conn))

(defn find-spec-one-register
  [db-conn]
  (d/q '[:find (pull ?product [*]) .
         :where [?product :product/id]
         [?product :product/price ?price]
         [(>= ?price 900)]] db-conn))

;(find-spec-one-register (d/db conn))

(def rules '[
             [(stock ?product ?price)
              [?product :product/price ?price]]
             ])

(defn find-by-product-rules
  [db-conn]
  (d/q '[:find (pull ?product [*])
         :in $ %
         :where [?product :product/id]
         (stock ?product ?price)
         [(>= ?price 5000)]
         ]
       db-conn rules)
  )

;(find-by-product-rules (d/db conn))

;(update-keys {:id 0} #(keyword (str "daily/" (name %))))
;(update-keys {:id 0 :name "a"} #(do
;                         (println %)
;                         (keyword (str "daily/" (name %)))
;                       )
;             )
;(name :name)
;(keyword "name")
;(str "daily-balance/" "name")
;(-> (str "daily-balance/" "name")
;    keyword)
;(update-in {:id {:id 9}} [:id :id] inc)
;(assoc-in {:id 0} [:id] 100)







