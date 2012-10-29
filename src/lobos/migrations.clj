(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema ))
  (:use (lobos.helpers)))

(defn surrogate-key [table]
  (integer table :id :auto-inc :primary-key))

(defn timestamps [table]
  (-> table
      (timestamp :updated_on)
      (timestamp :created_on (default (now)))))

(defn refer-to [table ptable]
  (let [cname (-> (->> ptable name butlast (apply str))
                  (str "_id")
                  keyword)]
    (integer table cname [:refer ptable :id :on-delete :set-null])))

(defmacro tbl [name & elements]
  `(-> (table ~name)
       (timestamps)
       ~@(reverse elements)
       (surrogate-key)))


;;; Defines the database for lobos migrations
(def torrentdb
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "torrent"
   :user "torrent"
   :password "torrent"
   :unsafe true})

(defmigration add-source-table
  (up [] (create torrentdb
           (tbl :sources (integer :id :primary-key :auto-inc)
             (varchar :name 100 :unique )
             (varchar :url 100 :not-null )
             (varchar :template 255 :not-null))))
  (down [] (drop (table :sources ))))

(defmigration add-torrents-table
  (up [] (create torrentdb
           (tbl :torrents (integer :id :primary-key :auto-inc)
                  (timestamp :sample-date )
                  (varchar :title 100 :unique )
                  (bigint :seeders  :not-null )
                  (bigint :leechers  :not-null))))
  (down [] (drop (table :sources ))))


;(use 'lobos.core 'lobos.connectivity )
;(open-global torrentdb)
;(migrate)


(defmigration add-users-table
  (up [] (create
          (tbl :users
            (varchar :name 100 :unique)
            (check :name (> (length :name) 1)))))
  (down [] (drop (table :users))))
