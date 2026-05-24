package com.barbulescu.codedojo.exercise0001

class ExerciseKotlin0001 : Exercise0001 {

    override fun processWords(words: List<String?>): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 until words.size) {
            val word = words[i]
            if (word == null) continue
            result.add(word.uppercase())
        }
        return result
    }

    override fun countWords(words: List<String>): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        for (word in words) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word)!! + 1)
            } else {
                result.put(word, 1)
            }
        }
        return result
    }
}
