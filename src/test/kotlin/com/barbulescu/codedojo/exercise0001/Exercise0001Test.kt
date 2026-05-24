package com.barbulescu.codedojo.exercise0001

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Exercise0001Test {

    companion object {
        @JvmStatic
        fun implementations() = listOf(ExerciseJava0001(), ExerciseKotlin0001())
    }

    @ParameterizedTest
    @MethodSource("implementations")
    fun filterEven(exercise: Exercise0001) {
        assertEquals(listOf(2, 4, 6), exercise.filterEven(listOf(1, 2, 3, 4, 5, 6)))
    }

    @ParameterizedTest
    @MethodSource("implementations")
    fun toUpperCase(exercise: Exercise0001) {
        assertEquals(listOf("HELLO", "WORLD"), exercise.toUpperCase(listOf("hello", "world")))
    }

    @ParameterizedTest
    @MethodSource("implementations")
    fun sumPositive(exercise: Exercise0001) {
        assertEquals(6, exercise.sumPositive(listOf(-3, 1, -1, 2, 3)))
    }
}
