package com.barbulescu.codedojo.exercise0001

interface Exercise0001 {
    fun processWords(words: List<String?>): List<String>
    fun countWords(words: List<String>): Map<String, Int>
    fun splitByParity(numbers: List<Int>): Pair<List<Int>, List<Int>>
    fun extractWords(sentences: List<String?>): List<String>
}
