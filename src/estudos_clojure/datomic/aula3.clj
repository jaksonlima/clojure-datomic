(ns estudos-clojure.datomic.aula3
  (:require [clojure.walk :as walk]
            [datomic.api :as d]
            [schema.core :as s])
  (:use [clojure.pprint])
  (:import (java.util UUID)))

(def db-uri "datomic:dev://localhost:4334/hello")

(defn create-connection-db [uri]
  (d/create-database uri)
  (d/connect uri))

(defn delete-db [uri]
  (d/delete-database uri))

(def conn (create-connection-db db-uri))
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

                ; Category
                {:db/ident       :category/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :category/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}

                ;Test map ref
                ;Car
                {:db/ident       :car/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :car/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
                {:db/ident       :car/dependence
                 :db/valueType   :db.type/ref
                 :db/cardinality :db.cardinality/many}

                ;Dependence
                ;Tire
                {:db/ident       :tire/name
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :tire/brand
                 :db/valueType   :db.type/string
                 :db/cardinality :db.cardinality/one}
                {:db/ident       :tire/id
                 :db/valueType   :db.type/uuid
                 :db/cardinality :db.cardinality/one
                 :db/unique      :db.unique/identity}
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
              (s/optional-key :product/category) Category})


(defn create-tire
  []
  [{:tire/name  "37R"
    :tire/brand "pirelli"
    :tire/id    (UUID/randomUUID)}
   {:tire/name  "45R"
    :tire/brand "goodyear"
    :tire/id    (UUID/randomUUID)}])

(defn create-car
  [dependence]
  {:car/name       "civic"
   :car/id         (UUID/randomUUID)
   :car/dependence dependence})

(defn create-tire-transact [data conn]
  (d/transact conn data))

;(create-tire-transact (create-tire) conn)

(defn create-car-transact [tire conn]
  (d/transact conn [(create-car [tire])]))

;(create-car-transact conn)

(defn add-car-type-tire [car-id tire-id]
  (d/transact conn [[:db/add car-id :car/dependence tire-id]]))

;(add-car-type-tire 17592186045451 17592186045454)

(defn query-all-car [db-conn]
  (d/q '[:find (pull ?car [* {:car/dependence [*]}])
         :where [?car :car/id]]
       db-conn))

(query-all-car (d/db conn))

(defn query-tire-all [db-conn]
  (d/q '[:find (pull ?tire [*])
         :where [?tire :tire/id]] db-conn))

;(query-tire-all (d/db conn))

(defn create-category []
  {:category/name "photo"
   :category/id   (UUID/randomUUID)})

(defn create-product []
  {:product/id       (UUID/randomUUID)
   :product/name     "zoom"
   :product/slug     "/zoom"
   :product/price    5000M
   :product/key      ["045" "007"]
   :product/category (create-category)})

;(s/validate Product (create-product))

(s/defn create-product-category-transact [data :- [Product] conn]
  (d/transact conn data))

(create-product-category-transact [(create-product)] conn)

(defn dissoc-id [entity]
  (walk/prewalk
    #(if (map? %) (dissoc % :db/id) %)
    entity))

(defn query-all [db-conn]
  (d/q '[:find [(pull ?category [* {:product/_category [*]}]) ...]
         :where [?category :category/id]]
       db-conn))

;(dissoc-id (query-all (d/db conn)))

;(dissoc {:db/id 001 :name "dissoc"} :db/id)

;(let [ip [:db/add "datomic.tx" :tx-data/ip "192.168.34.5"]]
;  (println ip)
;  )

(defn create-update
  [conn]
  (let [category {:category/id   (UUID/fromString "45f2f962-adac-44a0-8392-267b9947d8a8")
                  :category/name "intel-i9"}]
    (d/transact conn [category])))

;(create-update conn)

(defn remove [conn]
  (d/transact conn [[:db/retractEntity 17592186045434]]))

;(remove conn)

(defn remove-name
  [conn]
  (d/transact conn [[:db/retract 17592186045439 :category/name]]))

;(remove-name conn)

(defn add-name
  [conn]
  (d/transact conn [[:db/add 17592186045442 :product/key "3456"]]))

(add-name conn)

(s/defn find-of-id-category :- Category
  [id conn]
  (-> (d/q '[:find [(pull ?category [*]) ...]
             :in $ ?category-id
             :where [?category :category/id ?category-id]]
           conn id)
      dissoc-id
      first
      )
  )

;(find-of-id-category (UUID/fromString "501850dc-1b61-44b7-a5e9-37c0dc9e7ad0") (d/db conn))

(defn pull-query-category
  [id conn]
  (d/pull conn '[* {:product/_category [*]}] [:category/id id]))

;(pull-query-category (UUID/fromString "501850dc-1b61-44b7-a5e9-37c0dc9e7ad0") (d/db conn))

(defn pull-query-product
  [id conn]
  (d/pull conn '[* {:product/category [*]}] [:product/id id]))

;(pull-query-product (UUID/fromString "2dddb81a-8bff-4979-bab3-9e6756926c09") (d/db conn))

(def FindProductCategory {:db/id            s/Num
                          :product/name     s/Str
                          :product/slug     s/Str
                          :product/price    BigDecimal
                          :product/key      [s/Str]
                          :product/id       UUID
                          :product/category {:db/id         s/Num
                                             :category/id   UUID
                                             :category/name s/Str}})

(s/defn find-by-id-product! :- FindProductCategory
  [id :- java.util.UUID conn]
  (let [product (d/pull conn '[* {:product/category [*]}] [:product/id id])]
    (when (not (:product/id product))
      (throw (ex-info "not found product" {:product/id id})))
    product
    ))

(find-by-id-product! (UUID/fromString "2dddb81a-8bff-4979-bab3-9e6756926c09") (d/db conn))


















