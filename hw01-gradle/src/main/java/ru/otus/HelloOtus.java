package ru.otus;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelloOtus {
    public static void main(String... args) {
        int min = 0;
        int max = 100;
        List<Integer> example = Stream.iterate(min, n -> n + 1)
                .limit(max)
                .collect(Collectors.toList());

        System.out.println(Lists.reverse(example));
    }
}

