(defproject torrents "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.0.1" ;;; Exclude the clojure, clj-stacktrace from ring dependency
                  :exclusions [org.clojure/clojure
                               clj-stacktrace]]
                 [net.cgrand/moustache "1.1.0"]
                 [lobos "0.8.0"]
                 [lein-jdk-tools "0.1.0"]
                 [korma "0.2.1"]
                 [clj-time "0.4.4"]
                 [enlive "1.0.0"]
                 [postgresql "9.1-901.jdbc4"]
                 [clj-yaml "0.3.1"]]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"})
