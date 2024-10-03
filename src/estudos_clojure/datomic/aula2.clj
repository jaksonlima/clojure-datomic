(ns estudos-clojure.datomic.aula2
  (:require [datomic.api :as d])
  (:use [clojure.pprint])
  (:import (java.util UUID)))

(def db-uri "datomic:dev://localhost:4334/hello")

(defn create-connection-db [uri]
  (d/create-database uri)
  (d/connect uri))

(defn delete-db [uri]
  (d/delete-database uri))

(def conn (create-connection-db db-uri))
;;(pprint conn)
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

                ; Category
                {:db/ident       :category/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :category/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}]]
    (d/transact conn schema)))

;(create-schema conn)

(defn new-product
  ([name slug price keys] (new-product (UUID/randomUUID) name slug price keys))
  ([id name slug price keys]
   {:product/id    id
    :product/name  name
    :product/slug  slug
    :product/price price
    :product/key   keys}))

(defn new-category
  ([name] (new-category (UUID/randomUUID) name))
  ([id name]
   {:category/id id :category/name name}))

(defn create-new-or-update-product [products conn]
  (d/transact conn [products]))

(defn create-new-or-update-category [categories conn]
  (d/transact conn [categories]))

;(create-new-or-update-product (new-product "chip-intel" "/chip-intel" 8600.0M ["68" "98"]) conn)
;(create-new-or-update-product (new-product "phone" "/phone" 01.0M ["68" "98"]) conn)
;(create-new-or-update-product (new-product "mac" "/mac" 8600.0M ["57" "686"]) conn)

;(create-new-or-update-category (new-category "chip-intel") conn)
;(create-new-or-update-category (new-category "chip") conn)

(defn query-product-keywords-pulls-* [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?entity [*])
           :where [?entity :product/name]]
         db-query)))

;(query-product-keywords-pulls-* conn)
;(-> (query-product-keywords-pulls-* conn)
;    first
;    first
;    :product/id
;    .toString)

(defn query-find-by-db-id-product [db-id conn]
  (let [db-query (d/db conn)]
    (d/pull db-query '[*] db-id)))

;(query-find-by-db-id-product 17592186045418 conn)

(defn query-find-by-id-product-selected [id conn]
  (let [db-query (d/db conn)]
    (d/pull db-query '[:product/name :product/price :product/slug :product/key] id)))

;(query-find-by-id-product-selected 17592186045418 conn)

(defn query-find-by-product-id-product [product-id conn]
  (let [db-query (d/db conn)]
    (d/pull db-query '[*] [:product/id product-id])))

;(query-find-by-product-id-product (UUID/fromString "40019b2a-4ed1-4a4e-b78e-6e74f25e8fdb") conn)

(defn query-find-category [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?entity [*])
           :where [?entity :category/id]]
         db-query)))

;(query-find-category conn)

(defn update-product
  [conn]
  (d/transact conn
              [[:db/add
                [:product/id (UUID/fromString "986c6d64-d385-4932-aae5-9b0822a53cfd")]
                :product/category
                [:category/id (UUID/fromString "5c5dbe12-146a-433d-b248-21e55a80f4bc")]]]))

;(update-product conn)

(defn query-product-and-category [conn]
  (let [db-query (d/db conn)]
    (d/q '[:find ?p-name-value ?c-name-value
           :keys product category
           :where [?product :product/name ?p-name-value]
           [?product :product/category ?category]
           [?category :category/name ?c-name-value]] db-query)))

;(query-product-and-category conn)

(defn query-products-with-category-name-bind-keys [category-name conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?product [:product/name {:product/category [:category/name]}])
           :in $ ?name
           :where [?category :category/name ?name]
           [?product :product/category ?category]] db-query category-name)))

;(query-products-with-category-name-bind-keys "chip" conn)

(defn query-products-with-category-name-forward [category-name conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?product [* {:product/category [*]}])
           :in $ ?name
           :where [?category :category/name ?name]
                  [?product :product/category ?category]]
         db-query category-name)))

;(query-products-with-category-name-forward "chip" conn)

(defn query-products-with-category-name-backward [category-name conn]
  (let [db-query (d/db conn)]
    (d/q '[:find (pull ?category [* {:product/_category [*]}])
           :in $ ?name
           :where [?category :category/name ?name]]
         db-query, category-name)))

;(query-products-with-category-name-backward "Apple" conn)

(defn- new-nested-transact-product-and-category [conn]
  (d/transact conn [{:product/id       (UUID/randomUUID)
                     :product/name     "apple"
                     :product/slug     "/apple"
                     :product/price    1267M
                     :product/key      ["456" "378"]
                     :product/category {:category/id   (UUID/randomUUID)
                                        :category/name "Apple"}}]))

;(new-nested-transact-product-and-category conn)

(defn exists-nested-transact-product-and-category [conn]
  (d/transact conn [{:product/id       (UUID/randomUUID)
                     :product/name     "apple-m3"
                     :product/slug     "/apple-m3"
                     :product/price    1267M
                     :product/key      ["765" "8763"]
                     :product/category [:category/id (UUID/fromString "e92a7d44-8499-40df-aa96-0bc170e30518")]}]))

;;(exists-nested-transact-product-and-category conn)

(defn query-aggregates [conn]
  (let [db-conn (d/db conn)]
    (d/q '[:find (min ?price) (max ?price) (count ?price)
           :keys min max total
           :with ?product
           :where [?product :product/price ?price]]
         db-conn)))

;;(pprint (query-aggregates conn))

(defn query-aggregates-max-price [db-conn]
  (d/q '[:find (pull ?product [*])
         :where [(q '[:find (max ?price)
                      :where [_ :product/price ?price]] $) [[?price]]]
                             [?product :product/price ?price]]
       db-conn))

;;(pprint (query-aggregates-max-price (d/db conn)))

;;(pprint (ffirst (d/q '[:find (max ?price)
;;       :where [_ :product/price ?price]] (d/db conn))))


















