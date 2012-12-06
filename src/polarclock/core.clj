(ns polarclock.core
  (:gen-class)
  (:use quil.core)
  (:require [clj-time.core :as clj-time]))


(defn setup []
  (smooth)
  (no-fill)
  (frame-rate 1)
  (background 0))

(defn draw-radclock []
  (background 0)
  (let [diam  (* 0.9 (min (width) (height)))
        x     (* (width) 0.5)
        y     (* (height) 0.5)
        stoprad  (* TWO-PI (radians (clj-time/minute (clj-time/now))))
        ]
    (translate x y)
    (rotate (- HALF-PI))
    (stroke 0 255 0)
    (stroke-weight 20)
    (arc 0 0 diam diam 0 stoprad)
    (stroke 0 100 0)
    (stroke-weight 16)
    (arc 0 0 diam diam 0 stoprad)
  ))

(defsketch example
           :title "polar clock"
           :setup setup
           :draw draw-radclock
           :size [323 200])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
