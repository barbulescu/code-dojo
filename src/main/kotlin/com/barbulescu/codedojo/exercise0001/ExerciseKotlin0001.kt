package com.barbulescu.codedojo.exercise0001

class ExerciseKotlin0001 : Exercise0001 {

    override fun processWords(words: List<String?>) = words
        .filterNotNull()
        .map { it.uppercase() }

    override fun countWords(words: List<String>) = words
        .groupingBy { it }
        .eachCount()

    override fun splitByParity(numbers: List<Int>) = numbers.partition { it % 2 == 0 }

    override fun extractWords(sentences: List<String?>): List<String> = sentences
        .asSequence()
        .filterNotNull()
        .filter { it.startsWith("#") }
        .filter { it.length < 5  }
        .flatMap { it.split(" ") }
        .filter { it.length>2 }
        .toList()
}
