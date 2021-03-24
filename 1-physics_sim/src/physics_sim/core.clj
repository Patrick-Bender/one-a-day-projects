(ns physics_sim.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 60 frames per second.
  (q/frame-rate 60)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb)
  {:dt 0.05 :p_1 [50 0] :v_1 [20 -50] :p_2 [-150 0] :v_2 [-20 50] :m_1 1000 :m_2 1000}
  ; setup function returns initial state. It contains
  ; circle color and position.
  ;{:color 0
   ;:angle 0})
   )

(defn update-velocity [f v dt]
  [(+ (v 0) (* (f 0) dt)) (+ (v 1) (* (f 1) dt))]
)
(defn update-position [v p dt]
  [(+ (p 0) (* (v 0) dt)) (+ (p 1) (* (v 1) dt))]
)
(defn get-force [m_1 m_2 r]
  (def r_x (r 0))
  (def r_y (r 1))
  (def |r| 
    (Math/sqrt 
      (+ 
        (* (Math/abs r_x) (Math/abs r_x))
        (* (Math/abs r_y) (Math/abs r_y))
      )
    )
  )
  (def mag 
    (/ 
      (* m_1 m_2) 
      (* |r| |r|)
    )
  )
  [(* mag (/ r_x |r|)) (* mag (/ r_y |r|))]
)
(defn update-state [state]
  ; Update sketch state by changing circle color and position.
  (def r [(- ((state :p_2) 0) ((state :p_1) 0)) (- ((state :p_2) 1) ((state :p_1) 1))])
  (def f (get-force (state :m_1) (state :m_2) r))
  (def newv_1 (update-velocity f (state :v_1)  (state :dt)))
  (def newv_2 (update-velocity [(* (f 0) -1) (* (f 1) -1)] (state :v_2) (state :dt))) 
  {:dt (state :dt) 
   :p_1 (update-position newv_1 (state :p_1) (state :dt))
   :v_1 newv_1
   :p_2 (update-position newv_2 (state :p_2) (state :dt))
   :v_2 newv_2
   :m_1 (state :m_1) 
   :m_2 (state :m_2)}


)

(defn draw-state [state]
  ; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (let [t (/ (q/frame-count) 10)]
      (q/ellipse ((q/state :p_1) 0) ((q/state :p_1) 1) 20 20)
      (q/ellipse ((q/state :p_2) 0) ((q/state :p_2) 1) 20 20)
      


    )
  )
) 
(defn -main [& args]
  (println "working") 
)
(q/defsketch physics_sim
  :title "Physics Simulation"
  :size [500 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])
