package com.example.demo.utils;

public class ISBNValidator {

    public static boolean isValidISBN(String isbn) {
        if(isbn.length() != 13) {
            return false;
        }
        try {
            long isbnNumber = Long.parseLong(isbn);
            return getSum(isbnNumber) % 10 == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int getSum(long isbn) {
        int count = 1;
        int sum = 0;
        do {
            sum += count % 2 == 0 ? 3 * (isbn % 10) : isbn % 10;
            count++;
            isbn /= 10;
        } while (isbn > 0);
        return sum;
    }
}
