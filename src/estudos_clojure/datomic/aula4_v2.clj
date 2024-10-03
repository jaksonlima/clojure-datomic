(ns estudos-clojure.datomic.aula4_v2
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
              :product/digital                   s/Bool
              (s/optional-key :product/stock)    [Long]
              (s/optional-key :product/category) Category})

(defn create-category []
  {:category/name "os"
   :category/id   (UUID/randomUUID)})

(defn create-product []
  {:product/id       (UUID/randomUUID)
   :product/name     "mac"
   :product/slug     "/mac"
   :product/price    5000M
   :product/key      ["045" "007"]
   :product/digital  false
   :product/stock    34
   :product/category (create-category)})

(defn create-all [data conn]
  (d/transact conn data))

(def product (create-product))

;(create-all [product] conn)

(defn query-all-category-and-product [conn]
  (let [db-conn (d/db conn)]
    (d/q '[:find (pull ?category [* {:product/_category [*]}])
           :where [?category :category/id]]
         db-conn)
    )
  )

(query-all-category-and-product conn)

(defn remove-by-db-id [id conn]
  (d/transact conn [[:db/retractEntity id]]))

;(remove-by-db-id 1243 conn)

(defn query-product-with-category [categories conn]
  (let [db-conn (d/db conn)]
    (d/q '[:find (pull ?product [*])
           :in $ [?categories-value ...]
           :where [?category :category/name ?categories-value]
           [?product :product/category ?category]]
         db-conn categories)
    )
  )

;(query-product-with-category ["chip" "os"] conn)

(defn query-product-with-category-with-digital [categories digital conn]
  (let [db-conn (d/db conn)]
    (d/q '[:find [(pull ?product [*]) ...]
           :in $ [?categories-value ...] ?digital-value
           :where [?category :category/name ?categories-value]
           [?product :product/category ?category]
           [?product :product/digital ?digital-value]]
         db-conn categories digital)
    )
  )

(query-product-with-category-with-digital ["chip" "os"] false conn)

(defn transact-with-id [conn]
  (let [id (UUID/fromString "34fc65d4-22bc-40f2-9fbb-35c1cf3e261c")]
    (d/transact conn [[:db/add [:product/id id] :product/slug "/mac-os-lit-v2"]])
    )
  )

;(transact-with-id conn)

(defn transact-cas-with-id [conn]
  (let [id (UUID/fromString "34fc65d4-22bc-40f2-9fbb-35c1cf3e261c")]
    (d/transact conn [[:db/cas [:product/id id] :product/price 5000M 6000M]])
    )
  )

;(transact-cas-with-id conn)

(clojure.set/intersection
  (set (keys {:id 0 :name "jack"}))
  (set (keys {:id 1 :last-name "lima"})))

(defn transact-with-variation [product-id conn]
  (d/transact conn [{:db/id           "temporal-id"
                     :variation/id    (UUID/randomUUID)
                     :variation/name  "international-orlando"
                     :variation/price 1300M}
                    {:product/id        product-id
                     :product/variation "temporal-id"}]))

(transact-with-variation (:product/id product) conn)

(defn query-all-product-pull [product conn]
  (d/pull (d/db conn)
          '[* {:product/category [*]}]
          [:product/id (:product/id product)])
  )

(query-all-product-pull product conn)

(defn remove-all [product-id conn]
  (d/transact conn [[:db/retractEntity [:product/id product-id]]]))

;(remove-all (:product/id product) conn)

(s/defn create-visualization [product-id conn]
  (d/transact conn [{:product/id            product-id
                     :product/visualization 1}]))

;(create-visualization (:product/id product) conn)

(s/defn create-visualization-field [product-id conn]
  (d/transact conn [[:db/add [:product/id product-id] :product/visualization 5]]))

;(create-visualization-field (UUID/fromString "8e7bbfb9-b06a-40d7-84ec-3a3688242403") conn)

(s/defn find-by-id-product [product-id conn]
  (d/pull (d/db conn) '[*] [:product/id product-id]))

(find-by-id-product (UUID/fromString "8e7bbfb9-b06a-40d7-84ec-3a3688242403") conn)

(s/defn query-all-products [conn]
  (d/q '[:find (pull ?e [*])
         :where [?e :product/id]] (d/db conn)))

;(query-all-products conn)













