(ns estudos-clojure.aula18
  (:require [schema.core :as s])
  (:use [clojure.pprint])
  (:import (java.util Date)))

;(cs/valid? string? "100")
;(cs/valid? #(> % 5) 0)
;(cs/valid? inst? (Date.))
;(cs/valid? inst? 0)
;(UUID/randomUUID)
;(cs/valid? #{:id :name} :id)
;(cs/def :order/date inst?)
;(cs/def :deck/suit #{:id :name})
;(cs/valid? :order/date (Date.))
;(cs/conform :deck/suit :id)
;(cs/conform :order/date (Date.))
;(cs/conform :order/date 0)

(s/set-fn-validation! true)

(s/validate Long "20")

(s/defn valid-date
  [inst :- Date]
  (println inst))

(valid-date (Date.))

(s/defn test-simple [x :- Long]
  (println x))

(test-simple 10)
(test-simple "jack")

(s/defn new-patient
  [id :- Long, name :- s/Str]
  {:id id, :name name})

(new-patient "10" "jack")

(def Patient
  "Schema patient"
  {:id s/Num, :name s/Str})

(s/explain Patient)
(s/validate Patient {:id 0, :name "jack"})
(s/validate Patient {:id "0", :name "jack"})
(s/validate Patient {:id 0})

(s/defn new-patient
  [id :- s/Num, name :- s/Str]
  {:id id, :name name})

(new-patient 0 "jack")

(s/defn new-patient :- Patient
  [id :- s/Num, name :- s/Str]
  {:id id, :name name, :type "any"})

(new-patient 0 "jack")

(defn positive? [x] (> x 0))
(def Positive (s/pred positive?))

(s/validate Positive 0)
(s/validate Positive 1)
(s/validate Positive -1)
(s/validate (s/pred positive?) 10)

(def Patient
  {:id (s/constrained s/Int positive?), :name s/Str})

(def Patient
  {:id (s/constrained s/Int pos?), :name s/Str})

(s/validate Patient {:id 0, :name "lima"})
(s/validate Patient {:id 1, :name "lima"})