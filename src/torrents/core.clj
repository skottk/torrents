(ns torrents.core
  (:require [net.cgrand.enlive-html :as html])
  )


(defn fetch-url
  "Retrieves the web page specified by the url and
   makes an html-resource out of it which is used
   by enlive."
  [url] (html/html-resource (java.net.URL. url)))

(defn get-award-links [url]
  (map :attrs (html/select (fetch-url url)
                    #{[:div#content :li.page_item :a]
                      [:li.page.item.subtext html/first-child]})))


(def tpb-tpl
  { :url "http://thepiratebay.se/browse/601/0/9"
    :name [:table#searchResult :tr html/first-child]})

(defn fetch-tpl [nodes]
  (let [titles (html/select nodes
                            #{[:table#searchResult :td :a.detLink ]} )]
    (map (juxt
          #(get-in %1 [:attrs :title])
          #(get-in %1 [:attrs :href]))
         titles
         )))

(defn nth-indexed [idxs s]
  (map #(get %1 s)idxs ))


(defn get-indexed-tags [ row tag idxs]
  (map #(get-col-by-tag row tag %)
       idxs))




(def columns {
             ; :work [ 2 :a]
              :seeders [ 5 :content]
              :leechers [ 7 :content] })

(defn get-col-by-tag [row  tag idx]
  (tag (nth row idx)))

(defn get-tagged-cols [ row cols]
  (merge (map #(assoc {}
                  (key %)
                  (get-col-by-tag row
                                  (second (val %))
                                  (first (val %))) )
               cols)))


(defn get-keyed-tags [row tag idxs]
  (map #( tag ( nth row %))  idxs))

(defn get-tpb-rows [nodes]
  (butlast (rest (html/select nodes  #{[:table#searchResult :tr ]} ))))

(pprint
 (map
  #( get-tagged-cols % columns)
  (get-tpb-rows nodes)))

(pprint  (map
          #( get-tagged-cols % columns)
          (get-tpb-rows nodes)))
