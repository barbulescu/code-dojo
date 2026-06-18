package com.barbulescu.codedojo.exercise0001;

import org.jspecify.annotations.NonNull;

import java.util.*;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.partitioningBy;

public class ExerciseJava0001 implements Exercise0001 {

    @Override
    public @NonNull List<String> processWords(List<String> words) {
        return words.stream()
                .filter(Objects::nonNull)
                .map(String::toUpperCase)
                .toList();
    }

    @Override
    public @NonNull Map<String, Integer> countWords(List<String> words) {
        return words.stream()
                .collect(groupingBy(identity(), summingInt(w -> 1)));
    }

    @Override
    public kotlin.@NonNull Pair<List<Integer>, List<Integer>> splitByParity(List<Integer> numbers) {
        var partitioned = numbers.stream()
                .collect(partitioningBy(n -> n % 2 == 0));

        return new kotlin.Pair<>(partitioned.get(true), partitioned.get(false));
    }

    @Override
    public @NonNull List<String> extractWords(@NonNull List<String> sentences) {
        return sentences.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.startsWith("#"))
                .filter(s -> s.length() >= 5)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .filter(s -> s.length() > 2)
                .collect(toList());
    }
}
