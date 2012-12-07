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
  (stroke 0 75 25)
  (stroke-weight 1)
  (let [x     (* (width) 0.5)
        y     (* (height) 0.5)]
    (doseq [day (range 0 30)]
      (line 0 (* (height) (* 0.90 0.5))  0 (* (height) (* 0.95 0.5)))
      (rotate (radians-for-30d))
      )
    )
  (stroke-weight 3)
  (stroke 10 95 0)
  (pop-matrix)(push-matrix)
  (let [diam (* 0.95 (min (width) (height)))]
    (rotate (* (clj-time/day (clj-time/now)) (radians-for-30d)))
    (line 0 (* (height) (* 0.85 0.5))  0 (* (height) (* 0.95 0.5)))
    (rotate (- (radians-for-30d)))
    (line 0 (* (height) (* 0.85 0.5))  0 (* (height) (* 0.95 0.5)))
    (stroke-weight 5)
    (rotate HALF-PI)
    (arc 0 0 diam diam 0 (radians-for-30d))
    )
  (pop-style)
  (pop-matrix))

(defn draw-radclock []
  (push-matrix)
  (push-style)
  (let [diam  (* 0.8 (min (width) (height)))
        tmdiam (* 0.7 (min (width) (height)))
        sdiam (* 0.5 (min (width) (height)))
        x     (* (width) 0.5)
        y     (* (height) 0.5)
        stoprad  (minutes-to-radians (clj-time/minute (clj-time/now)))
        gmtrad  (hour-to-radians (clj-time/hour (clj-time/now)))
        hourrad  (hour-to-radians-12 (clj-time/hour (clj-time.local/local-now)))
        secrad  (minutes-to-radians (clj-time/sec (clj-time.local/local-now)))
        onerad (radians 1)
        minofhourrad (/ stoprad 14)
        ]
    (rotate (- HALF-PI))

    ; an hour-size mark for the hour
    ;(stroke 50 200 95)
    (stroke 0 220 20)
    (stroke-weight 3)
    (arc 0 0 tmdiam tmdiam hourrad (+ hourrad (radians 30))) ; 30 because 12h clock!
    ;   ;this shows 'hour markers' for a 24h clock
    ;    (doseq [x (range 0 23)]
    ;      (let [h (hour-to-radians x)
    ;            rdiff (radians 1)]
    ;            (arc 0 0 tmdiam tmdiam (- h rdiff) (+ h rdiff))))

    ; and an extra minute-dot on the hour...
    ; with a line out to the minutes ring
    (stroke-weight 5)
    (arc 0 0 tmdiam tmdiam (+ hourrad minofhourrad -0.01) (+ hourrad minofhourrad 0.01))

    ; the minutes ; draw two-part outline
    (stroke 0 220 20)
    (stroke-weight 3)
    (arc 0 0 diam diam 0 (+ minofhourrad hourrad))
    (let []
      (push-matrix)
      (rotate (+ minofhourrad hourrad (- HALF-PI)))
      (line 0 (* tmdiam 0.5)  0 (* diam 0.5))
      (pop-matrix)
      )

    (stroke 0 90 0)
    (stroke-weight 20)
    (arc 0 0 diam diam 0 stoprad)
    (stroke 0 220 20)
    (stroke-weight 16)
    (arc 0 0 diam diam 0 stoprad)

    ; let's see... red 'dot' on the GMT hour
    (stroke 250 25 25)
    (stroke-weight 5)
    (arc 0 0 tmdiam tmdiam (- gmtrad onerad) (+ gmtrad onerad))

    (stroke 0 53 10)
    (stroke-weight 50)
    (arc 0 0 sdiam sdiam (- secrad PI) (+ secrad PI))
    (stroke-weight 90)
    (stroke 0 50 10)
    (arc 0 0 sdiam sdiam (- secrad onerad) (+ secrad onerad))

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

(defn mksk []
  (defsketch example
           :title "polar clock"
           :setup setup
           :draw draw-stuff
           :size [500 500]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (mksk))

