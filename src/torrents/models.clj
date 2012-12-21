(ns torrents.models
  (:use korma.db
        korma.core))

(defdb torrentdb
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "torrent"
   :user "torrent"
   :password "torrent"})

(defentity torrents)
