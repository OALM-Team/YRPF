package fr.yuki.yrpf.utils;

public class Basic {
    public static int randomNumber(int min, int max) {
        return (int)(Math.random() * (max+1-min)) + min;
    }
}
