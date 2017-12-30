(ns ^:figwheel-no-load soundboard.dev
  (:require
    [soundboard.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
