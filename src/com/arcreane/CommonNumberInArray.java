package com.arcreane;

import javax.swing.text.Style;
import java.util.Arrays;
import java.util.Scanner;

public class CommonNumberInArray {

    public static void play() {
        int[] array1 = new int[5];
        int[] array2 = new int[5];
        int[] array3 = new int[5];

        fillArray(array1);
        fillArray(array2);
        fillArray(array3);

        checkCommonNumber(array1, array2, array3);
    }

    private static void checkCommonNumber(int[] p_iArray1, int[] p_iArray2, int[] p_iArray3) {
        //On prend notre premier tableau et on test élément par élément de ce tableau
        //p_iArray1 = [ 1, 2, 3, 4, 5]
        //p_iArray2 = [ 2, 4, 6, 5, 9]
        //p_iArray3 = [ 9, 5, 8, 3, 4]
        for (int i = 0; i < p_iArray1.length; i++) {
            //Pour chaque élément du tableau on compare à tous les élements du tableau 2
            //=> pour i = 0, p_iArray[0] vaut 1

            //=> pour i = 1, p_iArray[1] vaut 2

            //=> pour i = 2, p_iArray[2] vaut 3

            //=> pour i = 3, p_iArray[3] vaut 4
            for (int j = 0; j < p_iArray2.length; j++) {
                //Lorsque i vaut 0, on teste tous les éléments de p_iArray2 (c'est à dire 2,4,6,5 et 9)
                // et aucun n'est égale à 1 ( =  p_iArray[0]) on entre donc pas dans le if, dans le cas où i vaut 0

                //Lorsque i vaut 1, on teste tous les éléments de p_iArray2 (c'est à dire 2,4,6,5 et 9)
                // à l'index j = 0 on a  p_iArray2[j] = 2 on entre donc dans le if

                //Lorsque i vaut 2, on teste tous les éléments de p_iArray2 (c'est à dire 2,4,6,5 et 9)
                // et aucun n'est égale à 3 ( =  p_iArray[2]) on entre donc pas dans le if, dans le cas où i vaut 2

                //Lorsque i vaut 3, on teste tous les éléments de p_iArray2 (c'est à dire 2,4,6,5 et 9)
                // à l'index j = 1 on a  p_iArray2[j] = 4 on entre donc dans le if

                if(p_iArray1[i] == p_iArray2[j]){
                    //Lorsque i = 1 et j = 0, les 2 valeurs dans les tableaux à leur index respectif
                    //p_iArray1[1] et p_iArray2[0] valent tous les deux 2 on peut donc tester tous les éléments
                    // du tableau 3 pour voir si 2 est présent dans le tableau 3 (9, 5, 8, 3, 4)


                    //Lorsque i = 3 et j = 1, les 2 valeurs dans les tableaux à leur index respectif
                    //p_iArray1[3] et p_iArray2[1] valent tous les deux 4 on peut donc tester tous les éléments
                    // du tableau 3 pour voir si 4 est présent dans le tableau 3 (9, 5, 8, 3, 4)
                    for (int k = 0; k < p_iArray3.length; k++) {
                        //Lorsque i = 1 et j = 0 on regarde tous les éléments du tableau 3 mais aucun ne vaut 2
                        //on ne rentre donc pas dans ce if


                        //Lorsque i = 3 et j = 1 on regarde tous les éléments du tableau 3 et on s'apercoit qu'à
                        //l'index 4 on a une égalité entre p_iArray1[3] et p_iArray3[4] (et donc de p_iArray2[1] car
                        //on est dans le cas ou p_iArray1[3] =  p_iArray2[1] )
                        if(p_iArray1[i] == p_iArray3[k]){
                            //Lorsque i = 3, j = 1 et k = 4, dans les 3 tableaux la valeur à ces index respectif
                            //est de 4, ont peut donc afficher l'information à l'utilisateur
                            System.out.println("Number " + p_iArray1[i] +" is in the 3 arrays : ");
                            System.out.println("    *  in Array number 1 at index : " + i);
                            System.out.println("    *  in Array number 2 at index : " + j);
                            System.out.println("    *  in Array number 3 at index : " + k);
                        }
                    }
                }
            }
        }
    }

    private static void fillArray(int[] p_iArray) {
        boolean isOk = false;

        Scanner sc = new Scanner(System.in);
        while (!isOk) {
            System.out.println("Please enter your 5 distinct numbers separated by space (they must all be between 0 and 10");
            int i = 0;
            while (i < 5) {
                //i =0
                //"5 2 36 4 6"
                // ↑ comme il y a un espace après le 5, nextint renvoie juste 5 et déplace le curseur apres l'espace

                //i=1
                //"5 2 36 4 6"
                //   ↑ comme il y a un espace après le 2, nextint renvoie juste 2 et déplace le curseur apres l'espace

                //i=2
                //"5 2 36 4 6"
                //     ↑ comme il y a un espace après le 36, nextint renvoie juste 3 et déplace le curseur apres l'espace


                int temp = sc.nextInt();
                if (temp > 10) {
                    System.out.println("Number must be less than 10");
                    break;
                }

                //Apres l'echec de 5 2 36 4 6, l'utilisateur rentre 5 3 3 4 6
                if (checkIfInArray(p_iArray, temp, i)) {
                    System.out.println("Number must be different in a given serie");
                    break;
                }

                //Apres les échecs de 5 2 36 4 6 et 5 3 3 4 6, l'utilisateur rentre 1 2 3 4 5
                //qui respecte les consigne, on arrive donc à i = 5 et p_iArray complement rempli
                // et valant p_iArray = [1, 2 , 3, 4, 5]
                //i++ => on affecte d'abord la valeur de temp à l'index i,
                //puis on incrémente i
                p_iArray[i++] = temp;
            }
            if (i == 5)
                isOk = true;

            //Le nextline permet de supprimer le retour à la ligne que le nextint laisserait trainer
            sc.nextLine();
        }

    }

    private static boolean checkIfInArray(int[] p_iArray, int p_iTemp, int p_iLimitIndex) {
        //dans notre exemple, p_iArray [5,3,0,0,0], p_itemp = 3 et p_iLimitIndex = 2
        for (int j = 0; j < p_iLimitIndex; j++) {
            if (p_iArray[j] == p_iTemp)
                return true;
        }
        return false;
    }
}
