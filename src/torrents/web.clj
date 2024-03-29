(ns torrents.web
  (:use ring.adapter.jetty
        ring.middleware.resource
        ring.util.response
        net.cgrand.moustache))

;;; A simple handler to show send some response to the client.
(defn index
  [req]
  (response "Test web app "))

;; Routes definition
(def routes
  (app
    [""] index))

;;; start function for starting jetty
(defn start
([] (start 8080)) ; port - default to 8080
([port] (run-jetty #'routes {:port port :join? false})))
