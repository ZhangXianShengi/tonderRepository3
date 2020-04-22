package com.leyou.item.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class aaa {
    public static void main(String[] args) {
        Integer a = 71;
        Integer b = 72;
        Integer c = 73;
        List<Integer> as = Arrays.asList(a,b,c);
        for (Integer bc:as
             ) {
            System.out.println(bc);
        }
        as.stream().map(asd -> {
            System.out.println(asd);
            return asd;
        }).collect(Collectors.toList());
    }
}
