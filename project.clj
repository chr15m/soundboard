(defproject soundboard "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]
                 [reagent "0.7.0"]]

  :plugins [[lein-cljsbuild "1.1.5"]
            [lein-figwheel "0.5.14"]]

  :min-lein-version "2.5.0"

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :resource-paths ["public"]

  :figwheel {:http-server-root "."
             :nrepl-port 7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs ["public/css"]}

  :cljsbuild {:builds {:app
                       {:source-paths ["src" "env/dev/cljs"]
                        :compiler
                        {:main "soundboard.dev"
                         :output-to "public/js/app.js"
                         :output-dir "public/js/out"
                         :asset-path   "js/out"
                         :source-map true
                         :optimizations :none
                         :foreign-libs [{:file "https://raw.githubusercontent.com/chr15m/jsfxr/master/riffwave.js" 
                                         :provides ["riffwave"]}
                                        {:file "https://raw.githubusercontent.com/chr15m/jsfxr/master/sfxr.js" 
                                         :provides ["sfxr"]}]
                         :pretty-print  true}
                        :figwheel
                        {:on-jsload "soundboard.core/mount-root"
                         :open-urls ["http://localhost:3449/index.html"]}}
                       :release
                       {:source-paths ["src" "env/prod/cljs"]
                        :compiler
                        {:output-to "build/js/app.js"
                         :output-dir "public/js/release"
                         :asset-path   "js/out"
                         :optimizations :advanced
                         :foreign-libs [{:file "https://raw.githubusercontent.com/chr15m/jsfxr/master/riffwave.js" 
                                         :provides ["riffwave"]}
                                        {:file "https://raw.githubusercontent.com/chr15m/jsfxr/master/sfxr.js" 
                                         :provides ["sfxr"]}]
                         :pretty-print false}}}}

  :aliases {"package" ["do" "clean" ["cljsbuild" "once" "release"]]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.7"]
                                  [figwheel-sidecar "0.5.14"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]]}})