package com.ram.util;

/**
 * Created by Ravi on 21-Jun-19.
 */
public class RandomGenerator {
    public Long randomLong() {
        long leftLimit = 1L;
        long rightLimit = 1000000L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return generatedLong;
    }

}
