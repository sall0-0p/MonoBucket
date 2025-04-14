package com.lordbucket.bucketbank.util;

import java.security.SecureRandom;

public class CvcUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CVC_LENGTH = 3;

    public static String generateCvc() {
        int cvc = RANDOM.nextInt(1000);
        return String.format("%0" + CVC_LENGTH + "d", cvc);
    }
}
