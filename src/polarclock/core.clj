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

(defn days-in-this-month [] 30) ; TODO: HA HA THIS IS BROKEN

(defn minutes-to-radians [minutes]
  (radians (* 6 minutes)))

(defn radians-for-30d []
  (/ TWO-PI 30))

(defn draw-icon-at [r y]
  ;this is just sort of a dummy... it only draws a circle...
  (push-matrix)
  (push-style)
  (rotate r)
  (stroke 100)
  (stroke-weight 2)
  (ellipse 0 y 10 10)
  (pop-style)
  (pop-matrix))

(defn draw-monthicons [] ; MEGATODO: this.
  (push-matrix)
  (push-style)
  (draw-icon-at 0 (* (height) (* 0.85 0.5)))
  (pop-style)
  (pop-matrix) 
  )

(defn draw-monthclock []
  (rotate (- PI))
  (push-matrix)
  (push-style)
  (stroke 0 75 55)
  (stroke-weight 1)
  (let [x     (* (width) 0.5)
        y     (* (height) 0.5)]
    (doseq [day (range 0 30)]
      (line 0 (* (height) (* 0.8 0.5))  0 (* (height) (* 0.9 0.5)))
      (rotate (radians-for-30d))
      )
    )
  (stroke-weight 3)
  (stroke 0 95 75)
  (pop-matrix)(push-matrix)
  (rotate (* (clj-time/day (clj-time/now)) (radians-for-30d)))
    (line 0 (* (height) (* 0.75 0.5))  0 (* (height) (* 0.9 0.5)))
  (pop-style)
  (pop-matrix))

(defn draw-radclock []
  (push-matrix)
  (push-style)
  (let [diam  (* 0.6 (min (width) (height)))
        tmdiam (* 0.5 (min (width) (height)))
        x     (* (width) 0.5)
        y     (* (height) 0.5)
        stoprad  (minutes-to-radians (clj-time/minute (clj-time/now)))
        gmtrad  (hour-to-radians (clj-time/hour (clj-time/now)))
        hourrad  (hour-to-radians-12 (clj-time/hour (clj-time.local/local-now)))
        onerad (radians 1)
        ]
    (rotate (- HALF-PI))

    ; the minutes ; draw two-part outline
    (stroke 0 90 0)
    (stroke-weight 20)
    (arc 0 0 diam diam 0 stoprad)
    (stroke 0 220 20)
    (stroke-weight 12)
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

    ; and an extra minute-dot on the hour...
    (stroke-weight 4)
    (let [minofhourrad (/ stoprad 14)
          ]
      (arc 0 0 tmdiam tmdiam (+ hourrad minofhourrad -0.01) (+ hourrad minofhourrad 0.01)))

    (pop-style)
    (pop-matrix)
    ))

(defn draw-stuff []
  (let [x     (* (width) 0.5)
        y     (* (height) 0.5)]
    (translate x y))
;  (background 0 10 2)
  (background 0 50 10)
  (draw-radclock)
  (draw-monthclock)
;  (draw-monthicons)
  )

(defsketch example
           :title "polar clock"
           :setup setup
           :draw draw-stuff
           :size [500 500])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
