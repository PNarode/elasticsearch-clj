elasticsearch-clj
================

A Clojure client for [Elastic Search Cluster](https://www.elastic.co), a distributed, scalable analytic tool.

Follow the [download instructions on elastic.co]
(https://www.elastic.co/downloads) to install the latest version of Elastic Search.


Installation
------------

### Leiningen

elasticsearch-clj is distributed via [Clojars](https://clojars.org/org.clojars.pnarode/elasticsearch-clj). Add the
following to your dependencies in `project.clj`:

```clj
:dependencies [[org.clojars.pnarode/elasticsearch-clj "0.1.0"]]
```


Usage
-----

### Require in your app

```clj
(require '[elasticsearch-clj.core :as esclient])
```

