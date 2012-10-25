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

(map #(mapcat :content %1)
     (map #(drop 5 ( :content %1))
          (butlast (rest (html/select nodes  #{[:table#searchResult :tr ]} )))))

(defn get-indexed [s idxs]
  (mapcat #(get %1 s)idxs ))
