package com.arcreane;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean keepPlaying = true;

        while (keepPlaying) {
            System.out.println("choose between following options");
            System.out.println("1 - Hilo game");
            System.out.println("2 - Palindrome");
            System.out.println("3 - Common numbers in array");
            System.out.println("4 - MineSweep");
            System.out.println("5 - Quit");

            int userInput = getUserInput();
            switch (userInput) {
                case 1 -> {
                    Hilo.play();
                }
                case 2 -> {
                    Palindrome.play();
                }
                case 3 -> {
                    CommonNumberInArray.play();
                }
                case 4 -> {
                    MineSweep.play();
                }
                case 5 -> {
                    keepPlaying = false;
                }
                default -> {
                    System.out.println("Option not available, please try again");
                }
            }
        }
    }

    public static int getUserInput() {
        Scanner sc = new Scanner(System.in);

        int userInput = 0;
        try {
            userInput = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Sorry wrong input, please try again");
        }
        sc.nextLine();
        return userInput;
    }
}
