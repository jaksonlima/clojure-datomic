(ns estudos-clojure.datomic.aula1
  (:require [datomic.api :as d])
  (:use [clojure.pprint]))

(def db-uri "datomic:dev://localhost:4334/hello")

(defn create-connection-db [uri]
  (d/create-database uri)
  (d/connect uri))

(defn delete-db [uri]
  (d/delete-database uri))

(def conn (create-connection-db db-uri))
;(pprint conn)
;(pprint (delete-db db-uri))

(defn create-schema [conn]
  (let [schema [{:db/ident       :product/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/slug
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :product/price
                 :db/valueType   :db.type/bigdec
                 :db/cardinality :db.cardinality/one}
                {:db/ident :product/key
                 :db/valueType :db.type/string
                 :db/cardinality :db.cardinality/many}]]
    (d/transact conn schema)))

;(create-schema conn)

(defn new-product
  [name slug price, keys]
  {:product/name name :product/slug slug :product/price price :product/key keys})

(defn create-new-product [products conn]
  ;(let [create (d/transact conn [products])
  ;      id (-> @create :tempids vals first)]
  ;  id)
  (d/transact conn [products]))

;(create-new-product (new-product "tv" "/tv" 4200.0M ["1" "2"]) conn)
;(create-new-product {:product/name "phone" :product/slug "/phone" :product/price 8700.0M} conn)
;(create-new-product {:product/name "net" :product/slug "/net" :product/price 100.0M} conn)
;(create-new-product {:product/name "video" :product/slug "/video" :product/price 150.0M} conn)


(defn update-product [id conn]
  (d/transact conn [[:db/add id :product/price 0.1M]]))

;(as-> (create-new-product {:product/name "phone" :product/slug "/phone" :product/price 1100.0M} conn) id
;    (update-product id conn))

(defn remove-product [id conn]
  (d/transact conn [[:db/retract id :product/slug "/phone"]]))

;(as-> (create-new-product {:product/name "phone" :product/slug "/phone" :product/price 1100.0M} conn) id
;    (remove-product id conn))

(defn query-product [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?entity
           :where [?entity :product/price]] db-query)))

;(query-product conn)

(defn query-product-slug [slug conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?entity
           :in $ ?slug
           :where [?entity :product/slug ?slug]]
         db-query slug)))

;(query-product-slug "/phone" conn)

(defn query-product-slug-value [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?entity ?slug-value
           :in $
           :where [?entity :product/slug ?slug-value]]
         db-query)))

;(query-product-slug-value conn)

(defn query-product-slug-value-skip-entity [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?slug-value
           :where [_ :product/slug ?slug-value]]
         db-query)))

;(query-product-slug-value-skip-entity conn)

(defn query-product [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?entity ?price-value ?name-value
           :where [?entity :product/price ?price-value]
                  [?entity :product/name ?name-value]]
         db-query)))

;(query-product conn)

(defn query-product-keywords [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?entity ?price-value ?name-value
           :keys id price name
           :where [?entity :product/price ?price-value]
                  [?entity :product/name ?name-value]]
         db-query)))

;(query-product-keywords conn)

(defn query-product-keywords-pulls [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?entity [:product/name :product/slug :product/price])
           :where [?entity :product/name]]
         db-query)))

;(query-product-keywords-pulls conn)

(defn query-product-keywords-pulls-* [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?entity [*])
           :where [?entity :product/name]]
         db-query)))

;(query-product-keywords-pulls-* conn)

(defn snapshot-db [conn]
  (d/as-of (d/db conn) #inst "2024-09-12T23:01:22.012-00:00"))

;(count (snapshot-db conn))

(defn query-product-keywords-pulls-*-snapshot [conn]
  (let [db-query (snapshot-db conn)]
    (d/q '[:find (pull ?entity [*])
           :where [?entity :product/name]]
         db-query)))

;(query-product-keywords-pulls-*-snapshot conn)

(defn query-product-price [price conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?entity [*])
           :in $ ?price-param
           :where [?entity :product/price ?price]
                  [(> ?price-param ?price)]]
         db-query, price)))

(query-product-price 0 conn)

;(def data
;  [
;   [{:db/id 17592186045418, :product/name "phone", :product/slug "/phone", :product/price 0.1M}]
;   [{:db/id 17592186045426, :product/name "net", :product/slug "/net", :product/price 100.0M}]
;   ])
;(println (first (last data)))
;(vec (flatten data))
;(-> data flatten vec)
;(as-> "-" props
;      (println props)
;      (str props "1")
;      (println props)
;      (str props "-")
;      (println props)
;      )