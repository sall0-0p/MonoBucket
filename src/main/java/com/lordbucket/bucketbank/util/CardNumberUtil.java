package com.lordbucket.bucketbank.util;

public class CardNumberUtil {
    private static final String BANK_CODE = "44";

    public static String generateCardNumber(int accountId) {
        String accountPart = String.format("%05d", accountId);

        String withoutChecksum = BANK_CODE + accountPart;
        int checksum = calculateChecksum(withoutChecksum);
        return withoutChecksum + checksum;
    }

    public static boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 8) {
            return false;
        }

        String withoutChecksum = cardNumber.substring(0, 7);
        char expectedChecksum = cardNumber.charAt(7);
        int calcChecksum = calculateChecksum(withoutChecksum);
        return expectedChecksum == Character.forDigit(calcChecksum, 10);
    }

    public static int calculateChecksum(String digits) throws IllegalArgumentException {
        if (digits.length() != 7) {
            throw new IllegalArgumentException("Digits have to be 7 in length");
        }

        int sum = 0;
        int[] weights = {3, 5, 9, 7, 6, 5, 8};
        for (int i = 0; i < digits.length(); i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            sum += digit * weights[i];
        }
        int mod = sum % 10;
        return mod == 0 ? 0 : 10 - mod;
    }

    public static void main(String[] args) {
        String cardNumber = generateCardNumber(2);
        System.out.println(cardNumber);
        System.out.println(validateCardNumber(cardNumber));
        System.out.println(validateCardNumber("44003002"));
    }
}
