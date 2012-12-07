(ns polarclock.core
  (:gen-class)
  (:use quil.core)
  (:require [clj-time.core :as clj-time]
            [clj-time.local])
  (:import (java.util Calendar Date))
  )


(defn setup []
  (smooth)
  (no-fill)
  (frame-rate 1)
  (background 0 50 10))

(defn hour-to-radians [hr]
  ; there are 15 degrees "per hour" on a 24-hr clock
  (radians (* 15 hr)))

(defn hour-to-radians-12 [hr]
  (* 2 (hour-to-radians (if (> 12 hr) hr (- hr 12)))))

(defn minutes-to-radians [minutes]
  (radians minutes))

;(defn radians-for-this-month []
;  (

(defn draw-radclock []
  (background 180 200 120)
  (let [diam  (* 0.9 (min (width) (height)))
        tmdiam (* 0.8 (min (width) (height)))
        x     (* (width) 0.5)
        y     (* (height) 0.5)
        stoprad  (minutes-to-radians (clj-time/minute (clj-time/now)))
        gmtrad  (hour-to-radians (clj-time/hour (clj-time/now)))
        hourrad  (hour-to-radians-12 (clj-time/hour (clj-time.local/local-now)))
        onerad (radians 1)
        ]
    (translate x y)
    (rotate (- HALF-PI))

    ; the minutes ; draw two-part outline
    (stroke 50 200 95)
    (stroke-weight 20)
    (arc 0 0 diam diam 0 stoprad)
    (stroke 0 130 40)
    (stroke-weight 16)
    (arc 0 0 diam diam 0 stoprad)

    ; let's see... red 'dot' on the GMT hour
    (stroke 250 25 25)
    (stroke-weight 5)
    (arc 0 0 tmdiam tmdiam (- gmtrad onerad) (+ gmtrad onerad))

    ; an hour-size mark for the hour
    (stroke 50 200 95)
    (stroke-weight 1)
    (arc 0 0 tmdiam tmdiam hourrad (+ hourrad (radians 30))) ; 30 because 12h clock!
    (stroke-weight 1)
;   ;this shows 'hour markers' for a 24h clock
;    (doseq [x (range 0 23)]
;      (let [h (hour-to-radians x)
;            rdiff (radians 1)]
;            (arc 0 0 tmdiam tmdiam (- h rdiff) (+ h rdiff))))
  ))

(defsketch example
           :title "polar clock"
           :setup setup
           :draw draw-radclock
           :size [500 500])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
