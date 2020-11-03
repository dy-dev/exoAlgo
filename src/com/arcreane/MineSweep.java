package com.arcreane;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/***
 * Pseudo code
 * On récupère les info (de taille longueur et largeur , et le nombre de mines qu'il y aura dans le terrain)
 * On prépare l'affichage du terrain au lancement du jeu => appelé terrain de jeu
 * On prépare l'agencement des mines et des valeurs des cases dans le terrain qui ne sera connu que de
 * l'application et pas du joueur => appelé terrain secret
 *          On a le nombre de mines donné par l'utilisateur
 *          jusqu'à avoir placé toute les mines
 *              on choisi aléatoirement une coordonnée dans le terrain secret
 *              on vérifie qu'à cette coordonnée il n'y a pas déjà de mines
 *                  si c'est le cas on redémarre la boucle
 *                  sinon on place dans le terrain secret une nouvelle mines
 *          on va parcourir le tableau
 *              pour chaque mine qu'on va trouver
 *                  on va regarder les 8 cases qui l'entourent en faisant attention aux cas particuliers des mines aux bords
 *                  du tableau
 *                  et pour chacune de ces 8 cases on va incrémenter de 1 le nombre dedans (sauf si cette case contient
 *                  une mine)
 *                  Ce tableau va contenir des entiers ce sera donc un tableau d'int et pour représenter les mines on va
 *                  mettre -1 dans les cases qui contiennent des mines
 * On lance le jeu en proposant à l'utilisateur
 *          soit de retourner une case,
 *              Si c'est une mine il a perdu
 *                  Pour savoir si c'est une mine on compare la coordonée de l'utilisateur avec ce qu'il y a dans
 *                  le tableau secret. Si dans ce tableau c'est -1 alors c'était une mine et on annonce au joueur qu'il
 *                  a perdu
 *              Si c'est une case adjacente à une mine, on lui indique le nombre de mine autour
 *                  Pour savoir la valeur à afficher, on regarde dans le tableau secret ce qu'il y à la coordonée demandée
 *                  et on remplace le caracter sur le terrain du joueur par la valeur qu'il y a dans le tableau secret
 *              Si c'est une case sans mine adjacente, on découvre toutes les case sans mine adjacente, jusqu'à
 *              découvrir toutes les cases non vide autour de la case retournée
 *                  Si dans le tableau secret il y le chiffre 0 à la coordonnée demandée, alors on regarde tout autour
 *                  de cette coordonnée et on dévoile les cases autour d'elle
 *                      Si parmi les cases autour d'elle il y a des cases sans mine adjacente on recommence l'opération
 *                      avec cette case là, jusqu'à ce qu'il n'y ait plus de cases sans mine adjacente qui ne soit
 *                      pas dévoilée directement reliée à la case d'origine
 *          soit d'apposer sur la case un drapeau indiquant qu'il pense qu'il y a une mine en dessous
 *              => si il met un drapeau sur une case qui ne contient pas de mine il n'est pas prévenu de son erreur
 *
 *          Le joueur continue à retourner les cases jusqu'a ce qu'il n'y ai plus de case non retournée sur le plateau
 */


public class MineSweep {
    static int s_iWidth = 15;
    static int s_iHeight = 9;
    static int s_iMineNumber = 10;

    //Coordonnées courantes
    static int s_iX;
    static int s_iY;

    //Tableau qui va être afficher dans la console qui va contenir les cases non retournée
    //Les valeur sous forme de string des nombre de mines adjacente à la case
    //Les drapeaux des mines découvertes
    static String[][] s_PlayerMinefieldStringArray;

    //Tableau qui contient l'emplacement des mines (=> représenté par -1),
    //et les valeurs numériques du nombre de mines
    //adjacente à une case
    static int[][] s_MineFieldArray;

    static Scanner sc = new Scanner(System.in);
    static String s_sUnreturnedTile = "▉▉";
    static String s_sFlagTile = "\uD83C\uDFF4";


    public static void play() {
        System.out.println("What is the width you want");
        //s_iWidth = Main.getUserInput();
        System.out.println("What is the height you want");
        //s_iHeight = Main.getUserInput();
        System.out.println("How many mines do you want");
        //s_iMineNumber = Main.getUserInput();

        s_PlayerMinefieldStringArray = new String[s_iHeight][s_iWidth];
        for (int i = 0; i < s_PlayerMinefieldStringArray.length; i++) {
            for (int j = 0; j < s_PlayerMinefieldStringArray[i].length; j++) {
                s_PlayerMinefieldStringArray[i][j] = s_sUnreturnedTile;
            }
        }
        setupMineField();

        uncoverMines();
    }

    private static void uncoverMines() {
        boolean gameOver = false;

        while (!gameOver) {
            displayMineField();
             displaySecretMineField();

            //On demande à l'utilisateur ce qu'il veut faire
            //  soit retourner une case
            //  soit flagger une case
            String choice;
            do {
                System.out.println("What do you want to do?  Uncover a tile (U) or flag a mine (F)");
                choice = sc.nextLine();
            } while (!(choice.equals("U") || choice.equals("F")));  // <=> !choice.equals("U") && !choice.equals("F")
            if (choice.equals("U")) {
                uncoverMine();
            } else {
                flagMine();
            }
        }

    }

    private static void getCoord() {
        String[] coords = null;
        while (coords == null || coords.length != 2) {
            //Je récupère les coordonnées sous la forme X.Y et je sépare ces informations dans un tableaux de string
            //de la forme ["X", "Y"] (attention de la meme manière que les []le . est un character spécial dans les regexp
            //il faut donc l'utiliser avec le character d'échappement \\
            coords = sc.nextLine().split("\\.");
            if (coords.length != 2) {
                //Si il rentre non pas 6.0 mais 6,0 alors mon tableau sera de la forme ["6,0"]
                System.out.println("Wrong input format");
                continue;
            }
            //Ici par gain de temps et flemmardise il faut bien le dire, je ne vérifie pas que l'utilisateur a pas rentré
            //toto.tata ce qui flinguerait l'application
            s_iX = Integer.parseInt(coords[0]);
            s_iY = Integer.parseInt(coords[1]);

            //On vérifie que les coordonnée sont bien comprise dans le tableau pour pas faire des access hors scope
            if (s_iX >= s_iWidth || s_iY >= s_iHeight || s_iX < 0 || s_iY < 0) {
                System.out.println("Index out of bound please try again");
                coords = null;
            }
        }
    }

    private static void uncoverMine() {
        System.out.println("Where do you want to put your foot? (enter answer in the form X.Y)");
        getCoord();
        //soit il a choisi une mine et donc kaboom
        if (s_MineFieldArray[s_iY][s_iX] == -1) {
            System.out.println("KAAABBBOOOOOOMMMMM");
            displaySecretMineField();
        } else {
            discoverTile(s_iX, s_iY);
        }
    }

    private static void discoverTile(int p_iX, int p_iY) {
        //Si on a découvert une case adjacente à une mine alors on affiche simplement le nombre
        //de mines adjacentes
        if (s_MineFieldArray[p_iY][p_iX] != 0) {
            s_PlayerMinefieldStringArray[p_iY][p_iX] = " " + s_MineFieldArray[p_iY][p_iX];
        }
        //Sinon, si c'est une case qui n'a pas été retournée (car si elle a déjà été retournée il n'y a rien à faire)
        else if (s_PlayerMinefieldStringArray[p_iY][p_iX].equals(s_sUnreturnedTile)) {
            //Premier chose mettre un character vide à la place pour dire que la tuile a été retournée
            s_PlayerMinefieldStringArray[p_iY][p_iX] = "  ";
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    //Je regarde toute les tuiles autour de moi en faisant attention à ne pas sortir des limites du tableau
                    // et en évitant de boucler de manière infinie, je ne me regarde pas moi meme
                    if (    p_iY + i >= 0 && p_iX + j >= 0 &&
                            p_iY + i < s_iHeight && p_iX + j < s_iWidth &&
                            !(i == 0 && j == 0)) {
                        discoverTile(p_iX + j, p_iY + i);
                    }
                }
            }
        }
    }

    private static void flagMine() {
        System.out.println("Which tiles you think covers a mine? (enter answer in the form X.Y)");
        getCoord();
        s_PlayerMinefieldStringArray[s_iY][s_iX] = s_sFlagTile;
    }


    private static void setupMineField() {
        //On initialise le tableau
        s_MineFieldArray = new int[s_iHeight][s_iWidth];
        //On va compter le nombre de mines placée dans le tableau et on s'arrete lorsqu'il vaut le nombre de mines
        //choisi par l'utilisateur
        int temp = 0;
        Random rand = new Random();
        while (temp < s_iMineNumber) {
            //On choisit aléatoirement des coordonnées x et y variant entre 0 et la largeur/longeur
            int x = rand.nextInt(s_iWidth);
            int y = rand.nextInt(s_iHeight);

            //Si à la coordonnée choisi il n'y a pas déjà de mine
            if (s_MineFieldArray[y][x] != -1) {
                s_MineFieldArray[y][x] = -1;
                //On regarde les 8 cases qui entourent cette cases courante et on incrémente
                // les valeurs contenues dans ces cases
                //      A condition
                //          que ce ne soit pas une mine
                //          que ca ne déborde pas du tableaux
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (y + i >= 0 && x + j >= 0 &&
                                y + i < s_iHeight && x + j < s_iWidth) {
                            int tmp = s_MineFieldArray[y + i][x + j];
                            if (tmp != -1)
                                s_MineFieldArray[y + i][x + j]++;
                        }
                    }
                }
            }
            temp++;
        }
    }

    private static void displayMineField() {
        //[ligne 0 ]            => elle contient un tableau de string  [ ▉▉ | ▉▉ | ▉▉ | ▉▉ | ▉▉ ]
        //[ligne 1 ]            => elle contient un tableau de string  [ ▉▉ | ▉▉ | ▉▉ | ▉▉ | ▉▉ ]
        //[ ... ]               => elle contient un tableau de string  [ ▉▉ | ▉▉ | ▉▉ | ▉▉ | ▉▉ ]
        //[ligne s_iHeight ]    => elle contient un tableau de string  [ ▉▉ | ▉▉ | ▉▉ | ▉▉ | ▉▉ ]

        //A chaque ligne de mon tableau, on va concaténer le tableau de string qu'elle contient
        // en une seule string (en séparant chaque élément par des espaces)
        //Et on concatène toutes les lignes
        // (en ajoutant un character \n entre chaque pour revenir à la ligne à chaque fois)
        //Le but c'est d'avoir un string qui ressemble à ca
        // ▉▉ ▉▉ ▉▉ ▉▉ ▉▉
        // ▉▉ ▉▉ ▉▉ ▉▉ ▉▉
        // ▉▉ ▉▉ ▉▉ ▉▉ ▉▉
        // ▉▉ ▉▉ ▉▉ ▉▉ ▉▉
        //et on fera un appel à System.out.println sur ce string pour afficher le champ de mine en un seul appel
        //d'affichage qui est l'appel le plus couteux dans le programme
        StringBuilder displayField = new StringBuilder("");

        for (String[] lines : s_PlayerMinefieldStringArray) {
            //Arrays.toString est une fonction qui concatene tous les élements d'un
            // tableau en une string qui sera de la forme [element1, element2, element3,...]
            String line = Arrays.toString(lines);
            //String étant immutable, le replaceAll ne modifie pas la string elle meme mais
            //renvoie une nouvelle string avec les modifications. Il faut donc réaffecter cette
            //nouvelle string à notre variable (ou une nouvelle) pour avoir les modifications
            //Replaceall utilise ce qu'on appelle une expression régulière qui permet de retrouver
            // des character précis dans une chaine de character. Pour lister les character qui nous intéressent
            //on les donne entre []. Pour préciser qu'on recherche le character [, et qu'il ne doit pas être
            //interpréter par l'expression régulière comme une commande mais bien comme un simple character à rechercher
            // on doit utiliser le character d'échappement \\
            line = line.replaceAll("[,\\[\\]]", "");
            //la fonction append permet d'ajouter à la fin du string buffer un nouvel élément
            displayField.append(line).append("\n");
        }
        System.out.println(displayField);
    }

    private static void displaySecretMineField() {
        StringBuilder displayField = new StringBuilder("");

        for (int[] lines : s_MineFieldArray) {
            String line = Arrays.toString(lines);
            displayField.append(line).append("\n");
        }
        System.out.println(displayField);
    }

}
