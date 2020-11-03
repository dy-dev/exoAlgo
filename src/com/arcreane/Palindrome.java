package com.arcreane;

import java.util.Scanner;

public class Palindrome {
    public static void play() {
        System.out.println("Please enter your word or sentence you want to test :");
        Scanner sc = new Scanner(System.in);
        String palindromeWannaBe = sc.nextLine().toLowerCase();

        palindromeWannaBe = palindromeWannaBe.replaceAll(" ", "");

        boolean palindrome = isPalindromeWithFor(palindromeWannaBe);
        palindrome = isPalindromeWithStringFunctions(palindromeWannaBe);
        if (palindrome)
            System.out.println("The string (word or sentence) is a palindrome ");
        else
            System.out.println("This is not a palindrome");
    }

    private static boolean isPalindromeWithFor(String p_sPalindromeWannaBe) {
        System.out.println("Testing if string passed as a parameter is a palindrome " +
                "using a for loop (and 2 ways to manage the string, both ways being " +
                "equivalent)");
        int len = p_sPalindromeWannaBe.length();
        char[] stringAsCharArray = p_sPalindromeWannaBe.toCharArray();
        for (int i = 0; i < len / 2; i++) {
            if (stringAsCharArray[i] != stringAsCharArray[(len - 1) - i])
                return false;
            //Autre facon de faire totalement équivalent à la première
            if (p_sPalindromeWannaBe.charAt(i) != p_sPalindromeWannaBe.charAt(len - 1 - i))
                return false;
        }
        return true;
    }


    private static boolean isPalindromeWithStringFunctions(String p_sPalindromeWannaBe) {
        StringBuffer buffer = new StringBuffer(p_sPalindromeWannaBe);
        buffer.reverse();
        return p_sPalindromeWannaBe.equals(buffer.toString());
    }

}
