(ns test-1.intro.finance)


(def currencies {
                 :usd { :divisor 100 :code "USD" :sign "$"
                       :desc "US Dollars" }
                 :brl { :divisor 100 :code "BRL" :sign "R$"
                       :desc "Brazilian Real" }
                 :ukg { :divisor (* 17 29) :code "UKG" :sign "ʛ"
                       :desc "Galleons of the United Kingdom"}})

(def default-currency (:brl currencies))

(defn make-money
  "recebe uma quantidade e uma moeda e cria a entidade Money"
  ([]                {:amount 0
                      :currency default-currency})
  ([amount]          {:amount amount
                      :currency default-currency})
  ([amount currency] {:amount amount
                      :currency currency}))

(defn audit-transaction
  "método que cria um registro de auditoria para uma transação"
  [transaction]
  ; printing is a side-effect
  (println (str "auditing: " transaction))
  transaction)

(defn make-transaction
  "cria uma Transaction e gera um registro de auditoria"
  [trx-type account-id amount & details]
  (let [timestamp (quot (System/currentTimeMillis) 1000)
        transaction { :transaction-type :debit
                     :account-id       account-id
                     :details          details
                     :timestamp        timestamp
                     :amount           amount}]
    (audit-transaction transaction)))

(use '[clojure.repl :only [doc]])

(doc make-money)