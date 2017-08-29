package com.gpachov.apartmentsearch.mapper;

import java.util.Arrays;

/**
 * Created by Jore on 8/10/2017.
 */
public class TestMe {

    public static void main(String[] args) {
        test(new int[] {1, -2});
        test(new int[] {3,2,-1,2,5,4});
        test(new int[] {20_000,205_000,20_500_000,-2,-9,10_000_000, 2_000_000, 33_000_000});

    }

    private static void test(int[] T) {
        System.out.println(solution(T));
    }

    public static int solution(int[] T) {
        int[] maxEl = new int[T.length];
        maxEl[0] = T[0];
        for (int i = 1; i < maxEl.length; i++) {
            maxEl[i] = Math.max(T[i], maxEl[i-1]);
        }

        int[] minEl = new int[T.length];
        minEl[T.length-1] = T[T.length-1];
        for (int i = T.length -2; i >= 0; i--) {
            minEl[i] = Math.min(T[i], minEl[i+1]);
        }

        for (int i = 0; i < T.length-1; i++) {
            if (maxEl[i] < minEl[i+1]) {
                return i+1;
            }
        }
        return T.length -1;
    }
}
