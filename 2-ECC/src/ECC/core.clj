(ns ECC.core)

(defn discriminant [A B]
  (+ 
    (* 4 (* A (* A A)))
    (* 27 (* B B))
  )
)
;calculate inverse of x mod p
(defn inverseMod [x m & {:keys [check] :or {check 0}}]
  (println x m check)
  (if (< check m)
    (if (= 1 (mod (* check x) m))
      check
      (inverseMod x m :check (+ check 1))
      )
    -1
    
  )
)
(defn crossMod [P Q m]
  (let [m (* 
           (- (Q 1) (P 1))
           (inverseMod (- (Q 0) (P 0)) m))
        b (P 1)
        
        ]
      

  )
)
(defn multiplyMod [a P m]
  (if (= a 0) 
    P
    (multiplyMod (- a 1) (crossMod(P P m)) p)
  )
)
(defn encrypt [msg curve p]
)
(defn decrypt [msg curve p]
)
(defn -main [& args]
  (def m 13)
  (def A 3)
  (def B 8)
  ;For A = 3 and B = 8
  ;all possible P's are:
  ;(1,5) (1,8) (2,3) (2,10) (9,6) (9,7) (12,2) (12,11)
  (def P [2 10])
  (def msg "Hello World")
  ;n_a is Alice's secret key
  (def n_A 15)
  ;Q_A is Alice's public key
  (def Q_A 17)
  ;k is Bob's ephemeral key
  (def k (rand-int 30))
  "I don't do a whole lot."
  (println (discriminant -15 18))
  ;Should be 5
  (println (inverseMod 3 7))
  ;Should be 0
  (println (inverseMod 2 6))
  ; should be [-23/9 -170/27]
  ;(println (crossMod [7 16] [1 2] ))
)
  
