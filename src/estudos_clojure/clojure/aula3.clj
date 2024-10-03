(ns test-1.aula3)

(defn printed [] (println "jack"))

(defn aplica-desconto?
  [valor-bruto]
  (> valor-bruto 100))

(defn aplica-desconto?
  [valor-bruto]
  (when (> valor-bruto 100)
    true))

(aplica-desconto? 100.1)

(defn valor-desconto
  [valor-bruto fn-aplica-desconto]
  (if (fn-aplica-desconto valor-bruto)
    (let [taxa-desconto (/ 10 100)
          desconto (* valor-bruto taxa-desconto)]
      (- valor-bruto desconto))
    ))

(valor-desconto 101 #(> % 100))




