package com.barbulescu.codedojo.exercise0001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseJava0001 implements Exercise0001 {

    @Override
    public List<String> processWords(List<String> words) {
        List<String> result = null;
        result = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (word != null) {
                result.add(word.toUpperCase());
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> result = null;
        result = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1);
            } else {
                result.put(word, 1);
            }
        }
        return result;
    }

    @Override
    public kotlin.Pair<List<Integer>, List<Integer>> splitByParity(List<Integer> numbers) {
        List<Integer> evens;
        List<Integer> odds;

        evens = new ArrayList<>();
        odds = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            Integer number = numbers.get(i);
            if (number % 2 == 0) {
                evens.add(number);
            } else {
                odds.add(number);
            }
        }
        return new kotlin.Pair<>(evens, odds);
    }

    @Override
    public List<String> extractWords(List<String> sentences) {
        List<String> result = null;
        if (sentences != null || !sentences.isEmpty()) {
            result = new ArrayList<>();
            for (int i = 0; i < sentences.size(); i++) {
                String sentence = sentences.get(i);
                if (sentence != null && !sentence.startsWith("#") && sentence.length() >= 5) {
                    String[] words = sentence.split(" ");
                    for (int j = 1; j < words.length; j++) {
                        if (words[j].length() > 2) {
                            result.add(words[j]);
                        }
                    }
                }
            }
        }
        return result;
    }
}
