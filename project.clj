(defproject org.clojars.pnarode/elasticsearch-clj "0.1.0"
  :description "A Clojure client for Elastic Search "
  :url "http:/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  				 [org.clojure/data.json "0.2.6"]
  				 [clj-http "0.9.1"]
  				 [http-kit "2.1.18"]
  				 [cheshire "5.3.1"]]
  :main elasticsearch-clj.core)