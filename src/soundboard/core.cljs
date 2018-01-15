(ns soundboard.core
    (:require
      [reagent.core :as r]
      sfxr
      riffwave))

(enable-console-print!) 

(defonce sounds (r/atom (try (js->clj (js/JSON.parse (aget js/localStorage "soundboard"))) (catch :default e {}))))

(def hex "0123456789abcdef")

(def parameters
  ["e.a"
   "e.s"
   "e.p"
   "e.r"

   "♪"
   "♪_"
   "↕"
   "↕↕"

   "v"
   "v.↕"
   "a"
   "a.↕"

   "d"
   "d.↕"
   "f"
   "f.↕"

   "lo"
   "l.↕"

   "hi"
   "h.↕"

   "rez"
   "re"
   "▶"
   "x"])

(def notes ["C" "C#" "D" "D#" "E" "F" "F#" "G" "G#" "A" "A#" "B"])

(defn generate-sound [v]
  (try
    ((aget js/sfxr "toAudio") v)
    (catch :default e nil)))

(defn save! []
  (js/localStorage.setItem "soundboard" (js/JSON.stringify (clj->js @sounds))))

(defn add-empty-slot! [ev]
  (swap! sounds #(into {(js/Math.random) {"def" ""}} %))
  (save!))

(defn update-def! [id ev]
  (let [v (.. ev -target -value)
        sound (generate-sound v)]
    (swap! sounds assoc id {"def" v "sound" sound})
    (save!)))

(defn remove-sound! [id ev]
  (swap! sounds dissoc id)
  (save!))

(defn play-sound [synth-def]
  (let [snd (get synth-def "sound")]
    (when snd
      (.play snd))))

;; -------------------------
;; Views

; left arrow: ←
;

(defn page-settings []
  [:div
   [:button "x"]
   [:button "sync"]
   [:button.invisible ""]
   [:button.invisible ""]
   
   [:button.slider "nudge"]])

(defn page-note []
  [:div
   [:button "←"]
   [:button.invisible ""]
   [:button.invisible ""]
   [:button.invisible ""]
   
   (for [n notes]
     [:button n])
   
   (for [o (range 8)]
     [:button (str o)])])

(defn page-random []
  [:div
   [:button "←"]
   [:button.invisible ""]
   [:button "▶"]
   [:button "ok"]
   
   [:button "coin"]
   [:button "shot"]
   [:button "boom"]
   [:button "yay"]
   
   [:button "ouch"]
   [:button "boing"]
   [:button "blip"]
   [:button "synth"]
   
   [:button.slider "rnd"]])

(defn page-edit []
  [:div
   [:button "←"]
   [:button "rnd"]
   [:button "↑"]
   [:button "↓"]
   
   (for [i parameters]
     [:button (if (= i "") {:class "invisible"}) i])
   
   [:button.slider]])

(defn page-channels []
  [:div
   [:button "1"]
   [:button "[ 2 ]"]
   [:button "3"]
   [:button "4"]])

(defn page-home []
  [:div
   [:button.invisible "."]
   [:button "..."]
   [:button "?"]
   [:button (get ["▶" "■"] 0)]
   
   (doall
     (for [s (range 16)]
       [:button (str (get hex s) " .")]))])

(defn container []
  [page-random])

;; -------------------------
;; Initialize app

(doseq [[id s] @sounds]
  (print id s)
  (swap! sounds update-in [id] assoc "sound" (generate-sound (get s "def"))))

(defn mount-root []
  (r/render [container] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
