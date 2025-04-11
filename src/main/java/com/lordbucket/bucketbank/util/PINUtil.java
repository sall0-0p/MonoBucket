package com.lordbucket.bucketbank.util;

import java.security.SecureRandom;

public class PINUtil {
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generatePIN() {
        int pin = secureRandom.nextInt(10000); // generates a number from 0 to 9999
        return String.format("%04d", pin);
    }
}
