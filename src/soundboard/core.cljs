(ns soundboard.core
    (:require
      [reagent.core :as r]
      sfxr
      riffwave))

(defonce sounds (r/atom []))

(defn add-empty-slot! [ev]
  (swap! sounds #(into {(js/Math.random) {:def ""}} %)))

(defn update-def! [id s ev]
  (let [v (.. ev -target -value)
        sound (try (.getAudio (.generate (js/SoundEffect. v))) (catch :default e nil))]
    (swap! sounds assoc id {:def v :sound sound})))

(defn play-sound [synth-def]
  (.play (synth-def :sound)))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "sndbrd"]
   (for [[id s] @sounds]
     (if s
       (with-meta
         [:p [:input.def {:value (s :def)
                          :on-change (partial update-def! id s)} ]
          [:button {:on-click #(swap! sounds dissoc id)} "x"]
          [:button {:on-click (partial play-sound s)} "▶"]]
         {:key id})))
   [:p [:button {:on-click add-empty-slot!} "+"]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
