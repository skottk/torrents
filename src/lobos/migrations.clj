
(comment (ns lobos.migrations
           ;; exclude some clojure built-in symbols so we can use the lobos' symbols
           (:refer-clojure :exclude [alter drop
                                     bigint boolean char double float time])
           ;; use only defmigration macro from lobos
           (:use (lobos [migration :only [defmigration]]
                        core
                        schema))))

(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema
                )))

;;; Defines the database for lobos migrations
(def torrentdb
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "torrent"
   :user "torrent"
   :password "torrent"})

(defmigration add-source-table
  (up [] (create torrentdb
           (table :sources (integer :id :primary-key )
             (varchar :name 100 :unique )
             (varchar :url 100 :not-null )
             (varchar :template 255 :not-null))))
  (down [] (drop (table :sources ))))

(defmigration add-torrents-table
  (up [] (create torrentdb
           (table :torrents (integer :id :primary-key )
             (varchar :title 100 :unique )
             (bigint :seeders  :not-null )
             (bigint :leechers  :not-null))))
  (down [] (drop (table :sources ))))


;(use 'lobos.core 'lobos.connectivity )
;(open-global torrentdb)
;(migrate)
