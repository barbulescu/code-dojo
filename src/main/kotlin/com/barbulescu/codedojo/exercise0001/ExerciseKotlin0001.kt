package com.barbulescu.codedojo.exercise0001

class ExerciseKotlin0001 : Exercise0001 {

    override fun filterEven(numbers: List<Int>): List<Int> {
        val result = mutableListOf<Int>()
        for (n in numbers) {
            if (n % 2 == 0) {
                result.add(n)
            }
        }
        return result
    }

    override fun toUpperCase(words: List<String>): List<String> {
        val result = mutableListOf<String>()
        for (word in words) {
            result.add(word.uppercase())
        }
        return result
    }

    override fun sumPositive(numbers: List<Int>): Int {
        var sum = 0
        for (n in numbers) {
            if (n > 0) {
                sum += n
            }
        }
        return sum
    }
}
