package com.barbulescu.codedojo.exercise0001

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class Exercise0001Test {

    private val implementations = listOf(ExerciseJava0001(), ExerciseKotlin0001())

    @TestFactory
    fun exercise0001() = implementations.flatMap { exercise ->
        listOf(
            dynamicTest("${exercise::class.simpleName} processWords") {
                val actual = exercise.processWords(listOf("hello", null, "world"))
                val expected = listOf("HELLO", "WORLD")
                assertThat(actual).isEqualTo(expected)
            },
            dynamicTest("${exercise::class.simpleName} countWords") {
                val actual = exercise.countWords(listOf("a", "b", "c", "a", "c"))
                val expected = mapOf("a" to 2, "b" to 1, "c" to 2)
                assertThat(actual).isEqualTo(expected)
            },
            dynamicTest("${exercise::class.simpleName} splitByParity") {
                val actual = exercise.splitByParity(listOf(1, 2, 3, 4, 5))
                val expected = Pair(listOf(2, 4), listOf(1, 3, 5))
                assertThat(actual).isEqualTo(expected)
            },
            dynamicTest("${exercise::class.simpleName} extractWords") {
                val actual = exercise.extractWords(listOf("to hello world", null, "# comment", "hi", "to foo bar baz"))
                val expected = listOf("hello", "world", "foo", "bar", "baz")
                assertThat(actual).isEqualTo(expected)
            }
        )
    }
}
