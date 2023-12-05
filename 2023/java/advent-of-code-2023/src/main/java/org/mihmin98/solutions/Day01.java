package org.mihmin98.solutions;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 implements Solution {

    @Override
    public void solvePart1(List<String> input) {
        Pattern pattern = Pattern.compile("\\d");

        int result = input.stream()
                .map(pattern::matcher)
                .map(m -> m.results().toList())
                .map(l -> l.getFirst().group() + l.getLast().group())
                .mapToInt(Integer::parseInt)
                .sum();

        System.out.println(result);
    }

    @Override
    public void solvePart2(List<String> input) {
        Pattern pattern = Pattern.compile("(?=(\\d|one|two|three|four|five|six|seven|eight|nine))");

        Function<String, List<String>> extractMatches = s -> {
            Matcher m = pattern.matcher(s);
            return m.results()
                    .map(r -> s.substring(r.start(1), r.end(1)))
                    .toList();
        };

        int result = input.stream()
                .map(extractMatches)
                .map(l -> convertDigit(l.getFirst()) + convertDigit(l.getLast()))
                .mapToInt(Integer::parseInt)
                .sum();

        System.out.println(result);
    }

    private String convertDigit(String s) {
        return switch (s) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> s;
        };
    }
}
