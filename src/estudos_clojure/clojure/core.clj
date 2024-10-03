(ns test-1.core
  (:require [clojure.set :refer :all]
            [clojure.spec.alpha :as s]
            [test-1.intro.finance :as finance])
  (:import (java.time LocalDateTime))
  (:use [clojure.repl :only [doc]])
    (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn my-fn
  "my fn testing clojure"
  []
  (println "testing clojure....")
  0)

(my-fn)

(defn hello
  "my document"
  [name]
  (str "hello " name))

(hello "lima")

(fn [v] (str "hello " v))

(map (fn [v] (str "hello " v)) ["jack" "limaÞ"])

(map #(str "hello nu " %1) ["jack" "lima"])

(type :foo)

(str "Hello " "Jack")

(println "Hello" "Jack")

(do "foo" "bar")

(do
  (println "foo")
  "bar")

(fn [v]
  (do
    (println "name param is" name)
    (str "Hello " name)))

(fn [v]
  ((println "name param is" name)
   (str "Hello " name)))

(defn hello
  "my testing let"
  [v]
  (let [value "one value"
        separator " "
        catch v]
    (str value separator catch)))

(hello "lima")

(defn hello-aridades
  "my fn aridade"
  ([name]
   (str "hello fn-1 " name))
  ([name last-name]
   (str "hello fn-2 " name " " last-name)))

(hello-aridades "lima" "jack")

(defn hello-world
  "Dado um valor String "
  ([name]
   (str "Hello " name))
  ([firstname lastname]
   (hello-world (str firstname " " lastname))))

(defn hello
  "vargs"
  [& names]
  (println "=>" names)
  (clojure.string/join "" (cons "Hello " names)))

(hello "lima" " jack")

(cons "Hello " ["lima"])

(defn hello
  "&"
  [first & more]
  (clojure.string/join " " (conj more (clojure.string/upper-case first) "hello")))

(hello "lima" "jack")

(defn hello-world
  "Dada a sequência de parâmetros de nomes em String, retorna uma mensagem hello em String com esses nomes."
  [first-name & more-names]
  (clojure.string/join " " (conj more-names (clojure.string/upper-case first-name) "Hello")))

(defn hello-world
  "Dada a sequência de parâmetros de nomes em String, retorna uma mensagem hello em String com esses nomes."
  [& [first-name & more-names :as names]]
  (println "Received" (count names) "names for" first-name)
  (clojure.string/join " " (conj more-names (clojure.string/upper-case first-name) "Hello")))

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
  hello em String para cumprimentar o colaborador."
  [person]
  (let [strings (remove nil? ["Hello" (:first-name person) (:middle-name person) (:last-name person)])]
    (clojure.string/join " " strings)))

(hello-world {:first-name "Clair" :middle-name "de" :last-name "Lune" :employee-id "12345"})

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
    hello em String para cumprimentar o colaborador."
  [{:keys [first-name middle-name last-name] :as person}]
  (println "Person map with" (count person) "keys")
  (let [strings (remove nil? ["Hello" first-name middle-name last-name])]
    (clojure.string/join " " strings)))

(hello-world {:first-name "Clair" :middle-name "de" :last-name "Lune" :employee-id "12345"})

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
  hello em String para cumprimentar o colaborador."
  [{first-name :first-name middle-name :middle-name last-name :last-name}]
  (let [strings (remove nil? ["Hello" first-name middle-name last-name])]
    (clojure.string/join " " strings)))

(hello-world {:first-name "Clair" :middle-name "de" :last-name "Lune" :employee-id "12345"})

(defn my-destructured
  [{name :name last :last}]
  (println name last))

(my-destructured {:name "lima" :last "jack"})

(defn my-destructured
  [{:keys [name last]}]
  (println name last))

(my-destructured {:name "lima" :last "jack"})

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
  hello em String para cumprimentar o colaborador."
  [person]
  (let [defaults {:first-name "something" :middle-name "something" :last-name "something"}
        merged (merge defaults person)
        strings ["Hello" (:first-name merged) (:middle-name merged) (:last-name merged)]]
    (clojure.string/join " " strings)))

(hello-world {})

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
  hello em String para cumprimentar o colaborador."
  [{:keys [first-name middle-name last-name] :or {first-name "something" middle-name "something" last-name "something"}}]
  (let [strings ["Hello" first-name middle-name last-name]]
    (clojure.string/join " " strings)))

(hello-world {})

(defn hello-world
  "Dado um mapa que contém um colaborador, retorna uma mensagem
    hello em String para cumprimentar o colaborador."
  [{:keys [first-name middle-name last-name] :as person}]
  (println "Person map with" (count person) "keys")
  (let [strings (remove nil? ["Hello" first-name middle-name last-name])]
    (clojure.string/join " " strings)))

(hello-world {:first-name "Clair" :middle-name "de" :last-name "Lune" :employee-id "12345"})


(defn des
  [{:keys [name age] :as args}]
  (println "total " (count args))
  (println name " " age))

(des {:name "lima" :age 28})

(defn des2
  [{name :name age :age}]
  (println name " " age))

(des2 {:name "lima" :age 28})

(defn des3
  [args]
  (println args)
  (let [name (:name args)
        age (:age args)]
    (println name " " age)))

(des3 {:name "lima" :age 28})

(defn des4
  [arg1 {age :age}]
  (println arg1)
  (println age))

(des4 "lima" {:age 26 :last-name "jack"})

(merge {:name "jack"} {:age 29})

;;---------data-end-domain-entities--------------

(defn zero
  "this is a function that returns zero"
  []
  0)

(zero)

(type {:id 1})

(def person { :first-name "jack"
              :last-name "lima"
              :business-unit :clt
              :employee-id 12345 })

(:first-name person)

(defn upper [one] (println one) (clojure.string/upper-case one))
(defn upper-v [one two] (println two) (upper one))

(-> person
    (:first-name)
    (upper))

(->> person
    (:first-name)
    (upper-v "one-args"))

(as-> person p
      (p :first-name)
      (upper p))

(defrecord Employee [ first-name
                      last-name
                      business-unit
                      employee-id ])

(def person-record (map->Employee { :first-name "lima" }))

(def person-record-simple (->Employee "jack"
                                        "lima"
                                        :clt
                                        1234 ))

(assoc person-record :last-name "jack")

{ :customer-id 12345
 :first-name  "Maria"
 :last-name   "da Silva"
 :accounts [
            { :account-id "111-222-333"
             :account-type :checking }
            { :account-id "111-222-335"
             :account-type :credit }
            ]}

{ :account-id "111-222-333"
 :account-type :checking
 :customer {
            :customer-id 12345
            :first-name  "Maria"
            :last-name   "da Silva" }}

(assoc {:id 1} :name "lima")
(dissoc {:id 1 :name "jack"} :name)

(def currencies { :usd {:divisor 100 :symbol "USD"}
                  :brd {:divisor 100 :symbol "BR"} })
{ :amount 1200
  :currency :usd }

(defn make-money
  "recebe uma quantidade e uma moeda e cira a entidade Money"
  [amount currency]
  {:amount amount
   :currency currency})

(merge (make-money 10 "br") {:currency "us"})

(def default-currency (:brl currencies))

(defn make-money-
  "money instance"
  ([] {:amount 0
       :currency default-currency})
  ([amount] {:amount amount
             :currency default-currency})
  ([amount currency] {:amount amount
                      :currency currency}))

(make-money- 100 "br")

(def currencies {
                 :usd { :divisor 100 :code "USD" :sign "$"
                       :desc "US Dollars" }
                 :brl { :divisor 100 :code "BRL" :sign "R$"
                       :desc "Brazilian Real" }
                 :ukg { :divisor (* 17 29) :code "UKG" :sign "ʛ"
                       :desc "Galleons of the United Kingdom"}})

(/ 23 10)
(int (/ 23 10))
(rem 23 10)

(defn show-galleons
  "cria uma string de exibição para o dinheiro do Harry Potter"
  [amount]
  (let [{:keys [divisor code sign desc]} (:ukg currencies)
        galleons (int (/ amount divisor))
        less-galleons (rem amount divisor)
        sickles (int (/ less-galleons 17))
        knuts (rem less-galleons 29)]
    (str galleons " Galleons, " sickles " Sickles, and " knuts "Knuts")))

(defn show-money
  "cria uma string de exibição para a entidade Money"
  [{:keys [amount currency]}]
  (let [{:keys [divisor code sign desc]} (currency currencies)]
    (cond
      (= code "UKG")
        (show-galleons amount)
      :else
        (let [major (int (/ amount divisor))
              minor (mod amount divisor)]
          (str sign major "." minor)
          )
      )
    )
  )

(defn make-money
  "recebe uma quantidade e uma moeda e cria a entidade Money"
  [amount currency]
  (let [money {:amount amount
               :currency currency}]
    (-> money
        (assoc :displayed (show-money money)))))

(make-money 525 :ukg)

(defn audit-transaction
  "método que cria um registro de auditoria para uma transação"
  [transaction]
  ; println é um side-effect
  (println (str "auditing: " transaction))
  transaction)

(defn make-transaction
  "cria uma Transaction e gera um registro de auditoria"
  [trx-type account-id amount & details]
  (let [timestamp (quot (System/currentTimeMillis) 1000)
        transaction { :transaction-type trx-type
                     :account-id       account-id
                     :details          details
                     :timestamp        timestamp
                     :amount           amount}]
    (audit-transaction transaction)))

(quot (System/currentTimeMillis) 1000)

(make-transaction :debit "12345-01" (make-money 525 :ukg) "test")

(finance/make-money)

(some-> (LocalDateTime/now)
   (.toString))

(cond->> {:time (LocalDateTime/now)}
        true (println)
        false (println "error..")
        :else
        (println "else"))

(defn- same-currency?
  "`true` se as entidades Currency de Money são as mesmas"
  ([m1] true)
  ([m1 m2]
   (= (:currency m1) (:currency m2)))
  ([m1 m2 & monies]
   (every? true? (map #(same-currency? m1 %) (conj monies m2)))))

(defn- same-amount?
  "`true` se o `amount` das entidades Money forem os mesmos"
  ([m1] true)
  ([m1 m2] (zero? (.compareTo (:amount m1) (:amount m2))))
  ([m1 m2 & monies]
   (every? true? (map #(same-amount? m1 %) (conj monies m2)))))

(defn- ensure-same-currency!
  "lança uma exceção se as entidades Currency não são as mesmas, `true` caso contrário"
  ([m1] true)
  ([m1 m2]
   (or (same-currency? m1 m2)
       (throw
         (ex-info "Currencies do not match."
                  {:m1 m1 :m2 m2}))))
  ([m1 m2 & monies]
   (every? true? (map #(ensure-same-currency! m1 %) (conj monies m2)))))

(defn =$
  "`true` se as entidades Money são iguais"
  ([m1] true)
  ([m1 m2]
   (and (same-currency? m1 m2)
        (same-amount? m1 m2)))
  ([m1 m2 & monies]
   (every? true? (map #(=$ m1 %) (conj monies m2)))))

(defn +$
  "cria um objeto Money que representa a soma das entidades Money passadas como argumento"
  ([m1] m1)
  ([m1 m2]
   (ensure-same-currency! m1 m2)
   (make-money (+ (:amount m1) (:amount m2)) (:currency m1)))
  ([m1 m2 & monies]
   (apply ensure-same-currency! m1 m2 monies)
   (let [amounts (map :amount (conj monies m1 m2))
         new-amount (reduce + amounts)]
     (make-money new-amount (:currency m1)))))

(zero? 0)
(every? true? [true, true, true])
(.compareTo 1 1)
(conj ["dwa"] {:id 0})
(or true (println "false fn"))
(defn upper [v] (clojure.string/upper-case v))
;(apply upper "lima" "jack")
(apply + 10 5 [5 10])
(defn run
  ([v] (println v) (str v))
  ([v, b] (println v b) (str v b))
  ([v, b & more] (println v b more) (str v b more))
  )
(apply run "test " ["hello " "world"])

(s/def :money/amount int?)
(s/def :currency/divisor int?)
(s/def :currency/sign (s/nilable string?))
(s/def :currency/desc (s/nilable string?))
(s/def :currency/code (and string? #{"USD" "BRL" "UKG"}))

(s/def :finance/currency (s/keys :req-um [:currency/divisor
                                          :currency/code]
                                 :opt-um [:currency/sign
                                          :currency/desc]))

(s/valid? :finance/currency (:usd currencies))

(:brl currencies)

(map #(s/valid? :finance/currency %) (vals currencies))

(s/def :finance/money (s/keys :req-un [:money/amount
                                       :finance/currency]))

(s/valid? :finance/money {:amount 1 :currency (:brl currencies)})

(s/explain :finance/money {:amount 1 :currency (:brl currencies)})

(def numbers [1 2 3 4 5 6])
(->> numbers
     (filter odd?)
     (map #(* % %))
     (clojure.string/join ", "))
(first numbers)
(last numbers)
(get numbers 4)
(rest numbers)

(def my-list '(1 2 3 4 5))
(->> my-list
     (filter odd?)
     (map #(* % %))
     (clojure.string/join ", "))
(first my-list)
(last my-list)
(get my-list 0)
(rest my-list)
(count my-list)

(+ 1 2 3)

(set '(1 2 3 1))
#{1 1 2}

(def set-1 #{:a :b})
(subset? #{:b :a} set-1)

(intersection #{:a} #{:a :b})
(difference #{:a :c} #{:a :b})
(union #{:a} #{:a :b})
(hash-map :name "lima" :age 12)
(zipmap [:name :age :last] ["lima" 27 "jack" 89])
(class (first {:name "lima" :age 27}))
(key (first {:name "lima" :age 27}))
(val (first {:name "lima" :age 27}))

(not-any? #(> % 10) [1 2 3 4 5 6])
(some #{2 3 4} [3 4 5])

(filter int? ["a" :a 1 2])
(drop 1 [1 2 3 4 5])
(take 3 [1 2 3 4 5])

(reduce + 1 [1 2 3 4])
(reduce #(str %1 ", " %2) "->" '(:apple :oranges :pears))

(defn fib-next
  "gera a próxima par de sequencia de Fibonacci"
  [[a b]]
  [b (+ a b)])

(fib-next [0 1])

 (def fibonacci-sequence
  (map first (iterate fib-next [0 1])))

(take 10 fibonacci-sequence)
(take 5 fibonacci-sequence)
(doc comp)

(def odd-squares (comp (map #(* % %)) (filter odd?)))
(into [] odd-squares (range 21))

(def transaction-all [
                      {
                      :account-id       "12345-01"
                      :amount           {:amount   13000
                                         :currency :usd}
                      :details          {,,,}
                      :source           :transfer
                      :status           :settled
                      :timestamp        1654530232597
                      :transaction-type :debit
                      }
                      {
                       :account-id       "12345-01"
                       :amount           {:amount   12000
                                          :currency :usd}
                       :details          {,,,}
                       :source           :transfer
                       :status           :settled
                       :timestamp        1654530232597
                       :transaction-type :credit
                       }
                     {
                      :account-id       "12346-02"
                      :amount           {:amount   10000
                                         :currency :usd}
                      :details          {,,,}
                      :source           :deposit
                      :status           :pending
                      :timestamp        1654534231831
                      :transaction-type :debit
                      }
                      {
                       :account-id       "12346-011"
                       :amount           {:amount   50000
                                          :currency :usd}
                       :details          {,,,}
                       :source           :deposit
                       :status           :pending
                       :timestamp        1654534231831
                       :transaction-type :credit
                       }
                      {
                       :account-id       "12346-013"
                       :amount           {:amount   50000
                                          :currency :usd}
                       :details          {,,,}
                       :source           :deposit
                       :status           :pending
                       :timestamp        1654534231831
                       :transaction-type :credit
                       }] )

(def currencies { :usd {:divisor 100 :code "USD" :sign "$" :desc "UD Dollars"}
                 :brl {:divisor 100 :code "BRL" :sign "R$" :desc "Brazilian Real"} })

(def default-currency-brl (:brl currencies))

(defn make-money
  ([amount] {:amount amount :currency default-currency-brl})
  ([amount currency] {:amount amount :currency currency}))

(defn +$
  ([m1] m1)
  ([m1 m2]
   (ensure-same-currency! m1 m2)
   (make-money (+ (:amount m1) (:amount m2)) (:currency m1)))
  ([m1 m2 & monies]
   (apply ensure-same-currency! m1 m2 monies)
   (let [amounts (map :amount (conj monies m1 m2))
         new-amount (reduce + amounts)]
     (make-money new-amount (:currency m1)))))

(defn transactions-for-account-id
      [account-id txs]
  (filter #(= account-id (:account-id %1)) txs))

(defn filter-by-status
  [status txs]
  (filter #(= status (:status %1)) txs))

(defn filter-by-type
  [tx-type txs]
  (filter #(= tx-type (:transaction-type %1)) txs))

(defn total-settled-debits
  [account-id]
  (->> transaction-all
       (transactions-for-account-id account-id)
       (filter-by-status :debit)
       (map :amount)
       (reduce +$)))

(defn available-balance
  [account-id]
  (let [txs (transactions-for-account-id account-id transaction-all)
        settled-txs (filter-by-status :settled txs)
        debits (filter-by-type :debit settled-txs)
        credits (filter-by-type :credit settled-txs)
        debit-amts (map :amount debits)
        credit-amts (map :amount credits)
        neg-amts (map #(assoc %1 :amount (- (:amount %1))) credit-amts)
        amounts (concat debit-amts neg-amts)
        total-monies (reduce +$ amounts)]
    (println "-->>" total-monies)
    (cond
      (<= 0 (:amount total-monies)) total-monies
      :else (make-money 0 default-currency-brl)
      )))

(defn show-currency
  [account-id]
  (let [{:keys [amount currency] } (available-balance account-id)]
    {:amount amount
     :currency (currency currencies)}
    ))

;(make-money 0 default-currency-brl)
;(reduce +$ '({:amount 10000, :currency :usd} {:amount -12000, :currency :usd}))
;(map #(assoc %1 :amount (- (:amount %1))) '({:amount 12000, :currency :usd} {:amount 12000, :currency :usd}))

(defn pending-credits
  [txs]
  (let [txs-d txs]
    (->> txs-d
         (filter-by-type :credit)
         (filter-by-status :pending)
         (map :amount)
         (reduce +$))
    )
  )

(pending-credits transaction-all)

(total-settled-debits "12345-01")

(available-balance "12345-01")

(show-currency "12345-01")

(defn soma [a b c]
  (println a b c)
  (+ a b c)
  )

(def soma-with-ten (partial soma 10))

(soma-with-ten 1 1 )

(defn hire
  [employee]
  (assoc (update (assoc employee
                   :email (str (:first-name employee) "@nubank.com.br"))
                 :employment-history conj "Nubank")
    :hired-at (java.util.Date.)))

(defn hire
  [employee]
  (let [email (str (:first-name employee) "@nubank.com.br")
        employee-with-email (assoc employee :email email)
        employee-with-history (update employee-with-email :employment-history conj "Nubank")]
    (assoc employee-with-history :hired-at (java.util.Date.))))

(defn hire
  [employee]
  (-> employee
      (assoc :email (str (:first-name employee) "@nubank.com.br"))
      (update :employment-history conj "Nubank")
      (assoc :hired-at (java.util.Date.))
      ))

(hire {:first-name "lima"})
(hire {:first-name "Rich" :last-name "Hickey" :employment-history ["Cognitect"]})

(update {:name "lima"} :name #(clojure.string/upper-case %1))

(def employees
  [{:first-name "A" :last-name "B" :email "A@nubank.com.br" :hired-at #inst "2022-05-20"}
   {:first-name "C" :last-name "D" :email "C@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "E" :last-name "F" :email "E@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "G" :last-name "H" :email "G.H@nubank.com.br" :hired-at #inst "2022-05-21"}
   {:first-name "I" :last-name "J" :email "I.J@nubank.com.br" :hired-at #inst "2022-05-22"}])

(defn split-email-index-zero [email] (get (clojure.string/split email #"@") 0))

(defn email-address
  [employee]
  (format "%s.%s@nubank.com.br" (:first-name employee) (:last-name employee)))

(defn employees-incorrect-email
  [employees]
  (->> employees
       (filter #(= (:first-name %1) (split-email-index-zero (:email %1))))))

(defn old-email-format?
  [employee]
  (not= (:email employee) (email-address employee)))

(defn hired-day
  [employee]
  (.getDay (:hired-at employee)))

(defn employees-update-email
  [employees]
  (map #(assoc %1 :email (email-address %1)) employees))

(defn report
  [employees]
  (->> employees
       (filter old-email-format?)
       (map #(assoc % :email (email-address %)))
       (group-by hired-day)
       vals))

{4 [{:first-name "A", :last-name "B", :email "A.B@nubank.com.br", :hired-at #inst"2022-05-20T00:00:00.000-00:00"}],
 5 [{:first-name "C", :last-name "D", :email "C.D@nubank.com.br", :hired-at #inst"2022-05-21T00:00:00.000-00:00"}
    {:first-name "E", :last-name "F", :email "E.F@nubank.com.br", :hired-at #inst"2022-05-21T00:00:00.000-00:00"}]}

([{:first-name "A", :last-name "B", :email "A.B@nubank.com.br", :hired-at #inst"2022-05-20T00:00:00.000-00:00"}]
 [{:first-name "C", :last-name "D", :email "C.D@nubank.com.br", :hired-at #inst"2022-05-21T00:00:00.000-00:00"}
  {:first-name "E", :last-name "F", :email "E.F@nubank.com.br", :hired-at #inst"2022-05-21T00:00:00.000-00:00"}])

(report employees)
(employees-incorrect-email employees)
(old-email-format? (get employees 4))
(hired-day (get employees 4))
(split-email-index-zero "j@nubank.com.br")
(-> (employees-incorrect-email employees)
    (employees-update-email))

(clojure.string/escape "@" "A@nubank.com.br")
(clojure.string/ends-with? "@" "A@nubank.com.br")
(clojure.string/includes? "@" "A@nubank.com.br")
(clojure.string/index-of "@" "A@nubank.com.br")
(clojure.string/split "A@nubank.com.br" #"@")

(-> {}
    (assoc :foo "bar")
    (update :foo keyword))

(range 5)
(defn sum [a b & res] (+ a b))
(apply sum 1 [2 2 3])

(->> (range 5)
     (map inc)
     (filter even?)
     (apply +))

(-> {:first-name "Rich" :last-name "Hickey"}
    :hired-at
    .getTime) ;; Erro de execução (NullPointerException)

(some-> {:first-name "Rich" :last-name "Hickey"}
        :hired-at
        .getTime) ;; => nil

(some-> {:first-name "Rich" :last-name "Hickey" :hired-at #inst "2020-07-23"}
        :hired-at
        .getTime) ;; => 1595462400000

(defn describe-number
  [n]
  (cond-> []
          (odd? n) (conj "odd")
          (even? n) (conj "even")
          (zero? n) (conj "zero")
          (pos? n) (conj "pos")
          )
  )

(describe-number 3) ;; => ["odd" "positive"]
(describe-number 4) ;; => ["even" "positive"]

(as-> {:ints (range 5)} $
      (:ints $)
      (map inc $)  ;; last position
      (conj $ 10)  ;; first position
      (apply + $)) ;; => 25

(as-> 10 $
      (dec $)
      (if (even? $)
        (dec $)
        (inc $))
      (* 2 $))
