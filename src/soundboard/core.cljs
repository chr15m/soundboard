(ns soundboard.core
    (:require
      [reagent.core :as r]
      sfxr
      riffwave))

(enable-console-print!) 

(defonce sounds (r/atom (try (js->clj (js/JSON.parse (aget js/localStorage "soundboard"))) (catch :default e {}))))

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

(defn home-page []
  [:div [:h2 "sndbrd"]
   (doall (for [[id s] @sounds]
     (if s
       (with-meta
         [:p [:input.def {:value (get s "def")
                          :on-change (partial update-def! id)} ]
          [:button {:on-click (partial remove-sound! id)} "x"]
          [:button {:on-click (partial play-sound s)} "▶"]]
         {:key id}))))
   [:p [:button {:on-click add-empty-slot!} "+"]]])

;; -------------------------
;; Initialize app

(doseq [[id s] @sounds]
  (print id s)
  (swap! sounds update-in [id] assoc "sound" (generate-sound (get s "def"))))

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
