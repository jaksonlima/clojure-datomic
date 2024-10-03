(defproject estudos-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [prismatic/schema "1.4.1"]
                 [prismatic/schema-generators "0.1.5"]
                 [org.clojure/test.check "1.1.1"]
                 [com.datomic/peer "1.0.7187"]
                 [cheshire "5.10.1"]]
  :main ^:skip-aot test-1.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
