(ns estudos-clojure.aula17
  (:use [clojure.pprint])
  (:import (java.util List)
           (java.util Date)))

(defn add-user
  [map user]
  (if-let [id (:id user)]
    (assoc map id user)
    (throw (ex-info "User not be null 'id'" {:user user}))))

(defn tests-add-users
  []
  (let [empty-map {}
        user-one {:id 1 :name "jack"}
        user-two {:id 2 :name "lima"}
        user-tree {:name "wil"}]
    (pprint
      (-> empty-map
          (add-user user-one)
          (add-user user-two)
          (add-user user-tree)
          ))))

(tests-add-users)

(defrecord PrivatePatient [^Long id ^String name])
(defrecord HealthInsurancePatient [^Long id ^String name ^List plan])

(defprotocol IHealthPlan
  (sign-authorization? [patient procedure value]))

(extend-type HealthInsurancePatient IHealthPlan
  (sign-authorization?
    [patient procedure _]
    (let [plan (:plan patient)]
      (not (some #(= % procedure) plan))
      )))

(extend-type PrivatePatient IHealthPlan
  (sign-authorization?
    [_ _ value]
    (>= value 50)))

(-> (->HealthInsurancePatient 1 "jack" [:vaccine :exam])
    (sign-authorization? :covid 0))

(-> (map->HealthInsurancePatient {:id 11 :name "John" :plan [:x-ray]})
    (sign-authorization? :x-ray 0))

(-> (->PrivatePatient 2 "lima")
    (sign-authorization? 0 49))

;(->Patient 10 "jack")
;;(->Patient "a") -> error because need more arguments
;(map->Patient {:id 1 :name "lima"})
;(Patient. 1 "lima")
;(->Patient "01" "lima")

(defprotocol Datable
  (to-ms [this]))

(extend-type java.lang.Number Datable
  (to-ms [this] (+ 1 this)))

(extend-type java.util.Date Datable
  (to-ms [this] (.getTime this)))

(to-ms 10)
(to-ms (Date.))

(defn get-person [id]
  {:id id :date (.getTime (Date.))})

(defn load-person [cache id get-person]
  (if (contains? cache id)
    cache
    (assoc cache id (get-person id))))

(defprotocol Cache
  (load-of-id [this id]))

(defrecord Cacheable [cache] Cache
  (load-of-id [this id]
    (swap! (:cache this) load-person id get-person)
    this))

(as-> (->Cacheable (atom {})) my-atom
      (load-of-id my-atom 1)
      (load-of-id my-atom 2)
      (load-of-id my-atom 1)
      (pprint @(:cache my-atom)))

(let [my-ref {:ref-1 (ref ["blue" "red"])
              :ref-2 (ref {})}
      ref-1 (:ref-1 my-ref)
      ref-2 (:ref-2 my-ref)]

  (pprint ref-1)
  (pprint ref-2)

  (dosync
    (alter ref-1 pop)
    (alter ref-1 pop)
    (alter ref-1 conj "green")
    )

  (dosync
    (alter ref-2 assoc-in [:props :loaded] {:id 0})
    (alter ref-2 update-in [:props :loaded :id] inc)
    )
  )

;(swap! (atom {:id 1}) update :id inc)
;(update {:id 1} :id inc)
;(get {:id 1} :id 9)
;({1 {:id 1}} 0)
;(:id {:id 1})
;(contains? {1 {:id 1} 2 {:id 2}} 2)
;(contains? {:id 0} :id)


;(update {:id 0} :id inc)
;(update {:id 1} :id get-person)
;(assoc {1 (get-person 1)} 2 (get-person 2))
;(swap! (atom {}) my-update 2 get-person)
;(inc 1)

;(defn internal->domain [] (println "ok"))
;(defn domain->internal [] (println "ok"))
;(internal->domain)

(and (>= 49 49) (<= 11 11))
(not= 1 0)

(defrecord Private [id name])
(defrecord Health [id nome plan])

(defmulti sign-authorization-multi? class)
(defmethod sign-authorization-multi? Private
  [this]
  (println "private call")
  this)
(defmethod sign-authorization-multi? Health
  [this]
  (println "healt call")
  this)

(-> (->Private 0 "jack")
    (sign-authorization-multi?))

(-> (->Health 0 "lima" [:x-ray])
    (sign-authorization-multi?))

(defn printed [args] (println "printed") (println args))
(defmulti multi printed)
(defrecord Multi [id])
(defmethod multi Multi [args] (println "defmethod") (println args))

(multi "test")
(multi 0)
(multi :id)

(multi (->Multi 0))

(let [strategy { :2024 #(and (> % 50) (* 0.9 %)) }]
  (println ((:2024 strategy) 51))
  )

(and true (>= 1 1))



(* 10 0.5)
(* 0.5 10)





