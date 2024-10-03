(ns estudos-clojure.aula19
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Patient
  {:id PosInt, :name s/Str})

(s/defn new-patient :- Patient
  [id :- PosInt, name :- s/Str]
  {:id id, :name name})

(new-patient 1 "jack")
(new-patient -1 "jack")

(defn maior-ou-igual-a-zero? [x] (>= x 0))

(def Patient
  {:id (s/constrained s/Int maior-ou-igual-a-zero?)
   :name s/Str
   :type s/Keyword})

(def Patient-Process {:patient Patient, :process s/Keyword })

(s/validate Patient {:id 0, :name "jack" :type :normal})

(s/defn new-patient :- Patient-Process
  [patient :- Patient, process :- s/Keyword]
  {:patient patient, :process process})

(new-patient {:id 0, :name "jack" :type :normal}, :normal)

(def Numbers [s/Num])
(s/validate Numbers [10])
(s/validate Numbers ["10"])

(def Plans [s/Keyword])
(s/validate Plans [:vaccine])
(s/validate Plans ["vaccine"])

(def Plans [s/Keyword])
(def Patient {:id s/Num, :name s/Str, :plans Plans})
(s/defn new-patient :- Patient
  [plans :- Plans]
  {:id 1, :name "jack", :plans plans})

(new-patient [:vaccine, :exam])


(def Plans [s/Keyword])
(def Patient {:id s/Num, :name s/Str, :plans Plans, (s/optional-key :age) s/Str})
(s/defn new-patient :- Patient
  [plans :- Plans]
  {:id 1, :name "jack", :plans plans, :age "19/09/1997"})

(new-patient [:vaccine, :exam])


(def PosInt (s/pred pos-int?))
(def Plans [s/Keyword])
(def Patient {:id s/Num,
              :name s/Str,
              :plans Plans,
              (s/optional-key :age) s/Str})
(def Patients {PosInt Patient})
(s/defn new-patient :- Patients
  [plans :- Plans]
  {14 {:id 1, :name "jack", :plans plans, :age "19/09/1997"}})

(new-patient [:vaccine, :exam])

(s/defn new-patient-full :- Patient
  [patient :- Patient]
  {:id (:id patient),
   :name (:name patient),
   :plans (:plans patient),
   :age (:age patient)})

(new-patient-full {:id 1 :name "jack" :plans [:vaccine] :age "27"})




























