(ns perceptioncheck.core
  (:gen-class)
  (:import [opennlp.tools.sentdetect SentenceModel SentenceDetectorME])
  (:import [opennlp.tools.tokenize TokenizerModel TokenizerME])
  (:import [opennlp.tools.postag POSModel POSTaggerME])
  (:import [opennlp.tools.chunker ChunkerModel ChunkerME]))

(def sentence-detector
  (let [model-file (clojure.java.io/input-stream (clojure.java.io/resource "en-sent.bin"))
        model (SentenceModel. model-file)]
    (SentenceDetectorME. model)))

(defn sentences [document]
  (vec (.sentDetect sentence-detector document)))

(def tokenizer
  (let [model-file (clojure.java.io/input-stream (clojure.java.io/resource "en-token.bin"))
        model (TokenizerModel. model-file)]
    (TokenizerME. model)))

(defn tokens [document]
  (vec (.tokenize tokenizer document)))

(def pos-tagger
  (let [model-file (clojure.java.io/input-stream (clojure.java.io/resource "en-pos-maxent.bin"))
        model (POSModel. model-file)]
    (POSTaggerME. model)))

(defn tag [tokens]
  (vec (.tag pos-tagger tokens)))

(def chunker
  (let [model-file (clojure.java.io/input-stream (clojure.java.io/resource "en-chunker.bin"))
        model (ChunkerModel. model-file)]
    (ChunkerME. model)))

(defn chunks [tokens tags]
  (vec (.chunk chunker tokens tags)))

(defn tokens-chunked [tokens chunks]
  (partition-all 2 (interleave tokens chunks)))

(defn noun-phrase? [[_ chunk]]
  (or (= "B-NP" chunk) (= "I-NP" chunk)))

(defn filter-noun-chunks [sentence]
  (map #(map first %) 
       (filter (comp noun-phrase? last)
               (partition-by noun-phrase? sentence))))

(defn noun-phrases [document]
  (mapcat (fn [sentence]
    (let [tokens (tokens sentence) chunks (chunks tokens (tag tokens))]
      (filter-noun-chunks (tokens-chunked tokens chunks))))
    (sentences document)))

(defn seq-contains? [coll value]
  (loop [m coll acc false]
    (if (or (true? acc) (= '() m))
      acc
      (recur (rest m) (= (first m) value)))))

(defn -main [& args]
  (filter #(seq-contains? % "47th") (noun-phrases (first args))))

; (defn -main [& args]
;   (let [document (first args)
;         sentences-coll (sentences document)
;         first-sent-tokens (tokens (first sentences-coll))
;         first-sent-tags (tag first-sent-tokens)
;         first-sent-chunks (chunks first-sent-tokens first-sent-tags)
;         tokens-chunked (tokens-chunked first-sent-tokens first-sent-chunks)]
;     (println "Text to check: " document)
;     (println "Sentences: " sentences-coll)
;     (println "Tokens in first sentence: " first-sent-tokens)
;     (println "Tags in first sentence: " first-sent-tags)
;     (println "Chunks in first sentence: " first-sent-chunks)
;     (println "Noun Phrases: " (extract-noun-phrases tokens-chunked))))
