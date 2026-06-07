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
        for (i in 0 until words.size) {
            val word = words[i]
            if (result.containsKey(word)) {
                result.put(word, result.get(word)!! + 1)
            } else {
                result.put(word, 1)
            }
        }
        return result
    }

    override fun splitByParity(numbers: List<Int>): Pair<List<Int>, List<Int>> {
        val evens = mutableListOf<Int>()
        val odds = mutableListOf<Int>()
        for (i in 0 until numbers.size) {
            val number = numbers[i]
            if (number % 2 == 0) {
                evens.add(number)
            } else {
                odds.add(number)
            }
        }
        return Pair(evens, odds)
    }

    override fun extractWords(sentences: List<String?>): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 until sentences.size) {
            val sentence = sentences[i]
            if (sentence == null || sentence.startsWith("#") || sentence.length < 5) {
                continue
            }
            val words = sentence.split(" ")
            for (j in 1 until words.size) {
                if (words[j].length > 2) {
                    result.add(words[j])
                }
            }
        }
        return result
    }
}
