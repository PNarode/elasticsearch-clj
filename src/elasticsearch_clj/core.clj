(ns elasticsearch-clj.core
	(:gen-class)
	(:import
		[java.net URLEncoder])
	(:require
		[clj-http.client :as http-client]
		[clojure.set :as set]
		[clojure.data.json :as json])
	(:use
		[clojure.string :only [split]]))

;; Define default ES Cluster address.

(def address
	"Default ES Cluster address."
	{:host "localhost"
	 :protocol "http"
	 :port "9200"})

(defn create-address
	"Returns a map representing an ES address.
	  Valid Options:
	  	:host (default: \"localhost\")
	  	:protocol (default: \"http\")
	  	:port (default: 9200)"
	[opts]
	(merge address opts))


;; Connection function.
;; It checks whether the ES cluster with which you are trying to connect is active or not.

(defn connect
	"Connect function established a connection with ES cluster.
	 It takes map as an input which has following options.
	 Valid Options:
	 	:protocol (default: \"http\")
	  	:host (default: \"localhost\")
	  	:port (default: 9200)"
	[client]
	(let [address (create-address client)
		  url (str (address :protocol) "://" (address :host) ":" (address :port))]
		(try
			(http-client/get url {
					:socket-timeout 1000
					:conn-timeout 1000
					:content-type :json
					:throw-entire-message true})
		(catch Exception e
			(throw (Exception. "Connection with ES cluster cannot be established."))))
		url))


;; Returns Health of the cluster with Map of Cluster details like status, active shards, empty shards etc.

(defn cluster-health
	"Get Health of ES Cluster. Returns Raw HTTP response."
	[client]
	(let [url (str client "/_cluster/health")
		  output (http-client/get url {
					:socket-timeout 1000
					:conn-timeout 1000
					:content-type :json
					:throw-entire-message true})]
		  (json/read-str (output :body) :key-fn keyword)))


;; Returns status of health of the cluster like red, yellow or green.

(defn cluster-health-status
	"Get Health of ES Cluster. Returns only the status of ES Cluster."
	[client]
		((cluster-health client) :status))

;;Cluster health at Indice level.

(defn indice-health
	"Get Health of ES Cluster at indice level. Returns Raw HTTP response."
	[client & [indice]]
	(if (not indice)
		(let [url (str client "/_cluster/health?level=indices")
		  output (http-client/get url {
					:socket-timeout 1000
					:conn-timeout 1000
					:content-type :json
					:throw-entire-message true})]
		  ((json/read-str (output :body) :key-fn keyword) :indices))
		(let [url (str client "/_cluster/health/" indice)
		  output (http-client/get url {
					:socket-timeout 1000
					:conn-timeout 1000
					:content-type :json
					:throw-entire-message true})]
		  (json/read-str (output :body) :key-fn keyword))))


;; Function used to create a indices under the ES cluster.
(def setting
	"Default index setting."
	{:shards 5
	 :replicas 1})

(defn create-index
	"It takes 2 argumentsv client and data. Data has following options:
	 Valid Options:
	 	:name name of index to be created
	 	:shards No of shards under the index (Default is 5)
	 	:replicas No of replica under the shards (Default is 1 i.e One per primary shard)"
	[client temp]
	(let [data (merge temp setting)
		  url (str client "/" (data :name))
		  body (json/write-str {:setting {:number_of_shards (data :shards) :number_of_replicas (data :replicas)}})]
		(http-client/post url {
			:body body
			:socket-timeout 1000
			:conn-timeout 1000
			:content-type :json
			:throw-entire-message true})))

;; Function to delete index.

(defn delete-index
	"It deletes the specified index from the ES cluster."
	[client data]
	(let [url (str client "/" data "/")]
		(http-client/delete url {
			:socket-timeout 1000
			:conn-timeout 1000
			:content-type :json
			:throw-entire-message true})))


(defn -main
	[]
	(let [cluster {:host "localhost" :port 9200}
		  client (connect cluster)]
		;;(create-index client {:name "test"})
		(println (indice-health client))))

