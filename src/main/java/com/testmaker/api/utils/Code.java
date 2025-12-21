package com.testmaker.api.utils;

public class Code {
    public static int generate() {
        return (int) (Math.random() * (999999 - 100000)) + 100000;
    }
}
