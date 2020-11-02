package com.arcreane;

import java.util.Random;

public class Hilo {
    static int s_iGameType;

    public static void play() {
        s_iGameType = 0;
        Random rand = new Random();
        int numberToGuess = rand.nextInt(100);

        selectGameType();
        if(s_iGameType == 1)
            playLimited(numberToGuess);
        else if(s_iGameType == 2)
            playInfinite(numberToGuess);
        else
            System.out.println("Sorry wrong choice number");
    }


    private static void selectGameType() {
        System.out.println("What type of game do you want");
        System.out.println("1 - Only 10 tries to guess the number");
        System.out.println("2 - Play until you find the solution");

        while (s_iGameType != 1 && s_iGameType != 2) {
            s_iGameType = Main.getUserInput();
            if(s_iGameType != 1 && s_iGameType != 2){
                System.out.println("Wrong choice please try again");
            }
        }
    }

    private static void playLimited(int p_iNumberToGuess) {
       int i;
        for ( i = 0; i < 10; i++) {
            System.out.println("Please enter your proposition, " + (10 - i) +" try left");
            if (userTry(p_iNumberToGuess)) break;
        }
        if(i == 10)
            System.out.println("Sorry no more tries left, you lost");
    }

    private static void playInfinite(int p_iNumberToGuess) {
        int i = 1;
        do{
            System.out.println("Please enter your proposition (" + i++ + " try)");
        } while(!userTry(p_iNumberToGuess));
    }

    private static boolean userTry(int p_iNumberToGuess) {
        int userInput = Main.getUserInput();
        if(userInput < p_iNumberToGuess)
            System.out.println("Number to guess is bigger than " + userInput);
        else if(userInput > p_iNumberToGuess)
            System.out.println("Number to guess is smaller than " + userInput);
        else {
            System.out.println("Congratulation you found the number");
            return true;
        }
        return false;
    }



}
