package com.barbulescu.codedojo.exercise0001;

import java.util.ArrayList;
import java.util.List;

public class ExerciseJava0001 implements Exercise0001 {

    public List<Integer> filterEven(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        for (Integer n : numbers) {
            if (n % 2 == 0) {
                result.add(n);
            }
        }
        return result;
    }

    public List<String> toUpperCase(List<String> words) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            result.add(word.toUpperCase());
        }
        return result;
    }

    public int sumPositive(List<Integer> numbers) {
        int sum = 0;
        for (Integer n : numbers) {
            if (n > 0) {
                sum += n;
            }
        }
        return sum;
    }
}
