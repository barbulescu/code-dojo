# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a coding dojo — a practice repository for solving algorithmic/data-structure exercises in both **Kotlin** and **Java** side-by-side. Each exercise defines a shared interface, with two implementations (one in each language) and a single test class that validates both.

## Build & Test Commands

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run tests for a specific exercise
./gradlew test --tests "com.barbulescu.codedojo.exercise0001.*"

# Run a single test method
./gradlew test --tests "com.barbulescu.codedojo.exercise0001.Exercise0001Test.exercise0001"
```

## Architecture

Each exercise lives under a numbered package (e.g., `exercise0001`) spanning three source roots:

```
src/
  main/kotlin/.../exerciseNNNN/
    ExerciseNNNN.kt         # shared interface (Kotlin interface)
    ExerciseKotlinNNNN.kt   # Kotlin implementation
  main/java/.../exerciseNNNN/
    ExerciseJavaNNNN.java   # Java implementation (implements the Kotlin interface)
  test/kotlin/.../exerciseNNNN/
    ExerciseNNNNTest.kt     # single test class covering both implementations
```

The test class uses `@TestFactory` with JUnit 5 dynamic tests, iterating over `listOf(ExerciseJavaNNNN(), ExerciseKotlinNNNN())` so every test case runs against both implementations automatically.

## Adding a New Exercise

1. Create the interface in `src/main/kotlin/.../exerciseNNNN/ExerciseNNNN.kt`
2. Add the Kotlin implementation in the same package: `ExerciseKotlinNNNN.kt`
3. Add the Java implementation in `src/main/java/.../exerciseNNNN/ExerciseJavaNNNN.java` — it must implement the Kotlin interface directly
4. Add a single test class in `src/test/kotlin/.../exerciseNNNN/ExerciseNNNNTest.kt` using `@TestFactory` and dynamic tests over `listOf(ExerciseJavaNNNN(), ExerciseKotlinNNNN())`

## Stack

- Kotlin 2.4, JVM toolchain 25 (local), Java 21 (CI)
- JUnit Jupiter 6.x with `@TestFactory` for dynamic tests
- AssertJ for assertions
- Gradle with version catalog (`gradle/libs.versions.toml`)
