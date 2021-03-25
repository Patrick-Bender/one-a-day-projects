(ns ECC.core)

(defn discriminant [A B]
  (+ 
    (* 4 (* A (* A A)))
    (* 27 (* B B))
  )
)
;calculate inverse of x mod p
(defn inverseMod [x m & {:keys [check] :or {check 0}}]
  (if (< check m)
    (if (= 1 (mod (* check x) m))
      check
      (inverseMod x m :check (+ check 1))
      )
    -1
    
  )
)
(defn y [slope x b]
  (+ (* slope x) b)
  )
(defn slopeDerivative [x y A m]
  ;y' = 3x^2+A/(2y)
  (*
    (+ A (* 3 (* x x)))
    (inverseMod (* 2 y) m)
  )
)
(defn getSlope [P Q m A]
  (if (= P Q)
    (mod (slopeDerivative (P 0) (P 1) A m) m)
    (mod (* 
      (- (Q 1) (P 1))
      (inverseMod (- (Q 0) (P 0)) m))
      m
    )
  )
)
(defn add [P Q A B m]
    (let [slope (getSlope P Q m A)    
          b (- (P 1) (* slope (P 0)))
          y (fn [x] (+ (* slope x) b))
          ;x_3 = slope^2 - x_1 - x_2
          x_3 (mod (+ (* slope slope) (* -1 (P 0)) (* -1 (Q 0))) m)
          ]
        ;(println P Q slope b [x_3 (mod (* -1 (y x_3)) m)])
        [x_3 (mod (* -1 (y x_3)) m)] 
  )
)
;P original and p new, including two copies is ugly but it's easier than doing some fancy optional variable
(defn multiply [a P_o A B m & {:keys [P_n] :or {P_n P_o}}]
  (if (<= a 1) 
    P_n
    (multiply (- a 1) P_o A B m :P_n (add P_o P_n A B m))
  )
)
(defn -main [& args]
  (def m 13)
  (def A 3)
  (def B 8)
  ;For A = 3 and B = 8
  ;all possible P's are:
  ;(1,5) (1,8) (2,3) (2,10) (9,6) (9,7) (12,2) (12,11)
  (def P [2 10])
  ;Q_A is Alice's public key

  (if (= 0 (discriminant A B))
    (println "This is not an elliptic curve")
    (do
      (println "Should be [0 0]: " (multiply 9 [2 3] A B m))
      (println "Should be [12 11]: " (multiply 2 [2 3] A B m))
      (println "Should be [9 7]: "(multiply 3 [2 3] A B m))
      (println "Should be [1 5]: " (multiply 4 [2 3] A B m))
      (println "Should be [2 10]: " (multiply 8 [2 3] A B m))
      (println "Should be [2 3]: " (add [1 5] [9 6] A B m))
      (println "Should be [1 8]: " (add [12 2] [12 2] A B m))
    )
  )
)
  
