(ns torrents.core
  (:require [net.cgrand.enlive-html :as html])
  )


(defn fetch-url
  "Retrieves the web page specified by the url and
   makes an html-resource out of it which is used
   by enlive."
  [url] (html/html-resource (java.net.URL. url)))

(defn get-tpb-rows [nodes]
  (butlast (rest (html/select nodes  #{[:table#searchResult :tr ]} ))))

(def tpb-tpl
  { :url "http://thepiratebay.se/browse/601/0/9"
   :name [:table#searchResult :tr html/first-child]
   :columns {:work #(html/select
                  % #{[( html/nth-of-type 2) :a html/content]} )
          :seeders #(html/select
                     % #{[( html/nth-of-type 3) html/content]} )
          :leechers #(html/select
                      % #{[( html/nth-of-type 4) html/content]} ) }})

(defn get-tagged-cols [ row cols]
  (into {} (map #( vector
                  (key %)
                  (first ((val %) row)) )
               cols)))

(defn parse-tpb-page [tpl]
  (let [nodes (fetch-url (:url tpl))
        rows (get-tpb-rows nodes)]
    (map  #(get-tagged-cols % (:columns tpl)) rows)))
