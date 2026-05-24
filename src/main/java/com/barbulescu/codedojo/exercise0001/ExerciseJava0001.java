package com.barbulescu.codedojo.exercise0001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseJava0001 implements Exercise0001 {

    @Override
    public List<String> processWords(List<String> words) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (word == null) {
                continue;
            }
            result.add(word.toUpperCase());
        }
        return result;
    }

    @Override
    public Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> result = new HashMap<>();
        for (String word : words) {
            if (result.containsKey(word)) {
                result.put(word, result.get(word) + 1);
            } else {
                result.put(word, 1);
            }
        }
        return result;
    }
}
