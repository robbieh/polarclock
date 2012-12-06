(ns polarclock.core
  (:gen-class)
  (:use quil.core)
  (:require [clj-time.core :as clj-time]
            [clj-time.local]
            ))


(defn setup []
  (smooth)
  (no-fill)
  (frame-rate 1)
  (background 0))

(defn hour-to-radians [hr]
  ; there are 15 degrees "per hour" on a 24-hr clock
  (radians (* 15 hr)))

(defn minutes-to-radians [minutes]
  (* TWO-PI (radians minutes)))

(defn draw-radclock []
  (background 0)
  (let [diam  (* 0.9 (min (width) (height)))
        tmdiam (* 0.8 (min (width) (height)))
        x     (* (width) 0.5)
        y     (* (height) 0.5)
        stoprad  (minutes-to-radians (clj-time/minute (clj-time/now)))
        gmtrad  (hour-to-radians (clj-time/hour (clj-time/now)))
        hourrad  (hour-to-radians (clj-time/hour (clj-time.local/local-now)))
        ]
    (translate x y)
    (rotate (- HALF-PI))
    (stroke 0 255 0)
    (stroke-weight 20)
    (arc 0 0 diam diam 0 stoprad)
    (stroke 0 100 0)
    (stroke-weight 16)
    (arc 0 0 diam diam 0 stoprad)
    ; let's see... red 'dot' on the GMT hour
    (stroke 200 50 50)
    (stroke-weight 5)
    (arc 0 0 tmdiam tmdiam (- gmtrad (radians 1)) (+ gmtrad (radians 1)))
    ; an hour-size mark for the hour
    (stroke 0 255 0)
    (stroke-weight 1)
    (arc 0 0 tmdiam tmdiam hourrad (+ hourrad (radians 15)))
    
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
