(ns torrents.core
  (:require [net.cgrand.enlive-html :as html])
  (:require [torrents.models :as mod])
  (:require [korma.db :as db])
  (:require [korma.core :as kc])
  (:require [clj-time.core :as time])
  (:require [clj-time.coerce :as tc]))


(defn fetch-url
  "Retrieves the web page specified by the url and
   makes an html-resource out of it which is used
   by enlive."
  [url] (html/html-resource (java.net.URL. url)))


(defn get-tpb-rows [nodes]
  (butlast (rest (rest (html/select nodes  #{[:table#searchResult :tr ]} )))))

(defn get-page-tpb [pg]
  (let [url (str "http://thepiratebay.se/browse/601/" (- pg 1) "/9")]
    (get-tpb-rows
     (fetch-url url))))

(defn get-kat-rows [nodes]
  nodes)

(defn get-page-kat [pg]
  (let [url (str "http://kat.ph/books/"(- pg 1) "/?field=leechers&sorder=desc")]
    (get-kat-rows
     (fetch-url url))))




(def tpb-tpl
  { :url "http://thepiratebay.se/browse/601/0/9"
   :get-page get-page-tpb
   :columns {:title #(html/select
                  % #{[( html/nth-of-type 2) :a html/content]} )
          :size #(html/select
                     % #{[( html/nth-of-type 2) html/content]} )
             :seeders #(Integer/parseInt (html/select
                                          % #{[( html/nth-of-type 3) html/content]} ))
             :leechers #(html/select
                         % #{[( html/nth-of-type 4) html/content]} ) }})
(def kat-tpl
  { :url "http://kat.ph/books/1/?field=leechers&sorder=desc"
    :get-page get-page-kat
    :columns {:title #(html/select
                      % #{[( html/nth-of-type 2) :a html/content]} )
              :size #(html/select
                      % #{[( html/nth-of-type 2) html/content]} )
              :seeders #(html/select
                         % #{[( html/nth-of-type 3) html/content]} )
              :leechers #(html/select
                          % #{[( html/nth-of-type 4) html/content]} ) }})

(def templates [tpb-tpl])

(defn get-tagged-cols [ row cols]
  (into {} (map #( vector
                  (key %)
                  (first ((val %) row)) )
               cols)))

(defn parse-page [tpl pg]
  (let [rows ((:get-page tpb-tpl) pg)]
    (map  #(get-tagged-cols % (:columns tpl)) rows)))


(defn parse-rows [tpl rows]
  (map #(get-tagged-cols % (:columns tpl)) ))


(defn store-sample [row]
  (kc/insert mod/torrents
             (kc/values (into row {:sampledate (tc/to-sql-date (time/now))}))))

(defn store-page [tpl pg]
  (let [ rows (parse-page tpl pg)]
    (map store-sample rows)))

(defn store-rows [rows]
  (map store-sample rows))

(db/defdb torrentdb
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "torrent"
   :user "torrent"
   :password "torrent"})

(defn retrieve-pages [tpls]
  (doall (map
          #(map store-sample (parse-page % 1))
          tpls)))
