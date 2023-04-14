package view;
import controller.*;
import model.Tile;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Integer.valueOf;

public class UserInterface {
    Controller cont;

    public void run(){
        cont = new Controller();
        cont.createGame();
    } //this is probably wrong
    Scanner sc = new Scanner(System.in);

    //Boh, added this one just because
    public void greetings(){
        System.out.print("""
                Hi Welcome to MyShelfie!
                Do you want to read the rules?
                Press 1 for 'Yes', 0 for 'No'
                """);
        int ruleChoice = sc.nextInt();
        if (ruleChoice==1){
            //Here will be invoked a magic function that will show the rules :)
        } else if(ruleChoice==0){
            System.out.println("\nOk, let's go!");
        } else {
            while (ruleChoice!=0 || ruleChoice!=1) {
                System.out.println("Sorry, I don't understand, try again...");
                ruleChoice = sc.nextInt();
            }
        }
    }

    public int askNumOfPlayers(){
        System.out.print("How many players do you want to play with?\n Insert a number between 2 and 4:");
        return sc.nextInt();
    }

    public String askPlayerNickname(){
        System.out.print("\nChoose a nickname:");
        return sc.nextLine();
    }

    public int webProtocol(){
        System.out.print("\nChoose a comunication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
        return sc.nextInt();
    }

    public int userInterface(){
        System.out.print("\nDo you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?\n" +
                            "Press 1 for 'TUI', 2 for 'GUI':");
        return sc.nextInt();
    }

    public int askPlayAgain(){
        System.out.print("Dou you want to play again?\nPress 0 for 'No', 1 for 'Yes':");
        return sc.nextInt();
    }

    public int askColumn(){
        System.out.println("Choose the column, a cypher from 0 to 4:");
        return sc.nextInt();
    }//TODO in Controller check the input

    //With this function I ask the player the position of the first Tile it intends to choose,
    //if it chooses only one this function returns the coordinates of that tile
    public int[][] askFirstTilePosition(){
        ArrayList<Integer> boardColumn = new ArrayList<>();
        ArrayList<Integer> boardRow = new ArrayList<>();
        System.out.println("Choose the column, a cypher from 0 to 8:");
        boardColumn.add(sc.nextInt());
        if(boardColumn.get(0)<0 || boardColumn.get(0)>8){
            boardColumn.remove(0);
            boardColumn.add(sc.nextInt());}
        System.out.println("Choose the row, a cypher from 0 to 8:");
        boardRow.add(sc.nextInt());
        if (boardRow.get(0)<0 || boardRow.get(0)>8){
            boardRow.remove(0);
            boardRow.add(sc.nextInt());}
        int boardCoord [][] = {{boardColumn.get(0)},{boardRow.get(0)}};
        return boardCoord;
        //If the player choses a row or a column that is invalid (a negative one or one outside the board)
        // I set the coordinates so that boardCoord[][] = {{0},{0}}
    }//TODO in Controller check the input
    public int askNumberOfChosenTiles(){
        System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
        return sc.nextInt();
    }//TODO in Controller check the input

    public int askDirection(){
        System.out.println("In which direction do you want to pick the tiles?\nChoose one in between Up (0), Down(1), Left(2), Right(3):");
        return sc.nextInt();
    }//TODO in Controller check the input

    public void printTilesInHand(ArrayList<Tile> tilesInHand){
        System.out.print(tilesInHand);
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert one tile
    public int askTileToInsert(){
        System.out.println("Now you have to choose the disposition of the chosen tiles.\nThe first one on the left has an index 0, and so on...\n" +
                            "Digit a number (the index of the tile you want to put into the bookshelf):");
        return sc.nextInt();
    }//TODO in Controller check the input


}
