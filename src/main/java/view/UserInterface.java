package view;
import controller.*;
import model.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class UserInterface {
    Controller cont;

    public void run() throws IOException {
        cont = new Controller();
        cont.createGame();
    } //this is probably wrong
    BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
    //Boh, added this one just because
    public void greetings() throws IOException {
        System.out.print("""
                Hi Welcome to MyShelfie!
                Do you want to read the rules?
                Press 1 for 'Yes', 0 for 'No'
                """);
        int ruleChoice = Integer.parseInt(reader.readLine());
        boolean flag = false;
        while (!flag) {
            switch (ruleChoice) {
                case 1 -> {
                    flag = true;
                    System.out.println("Rules...");}
                //Here will be invoked a magic function that will show the rules :) TODO
                case 0 -> {
                    flag = true;
                    System.out.println("\nOk, let's go!");}
                default ->{
                    while (ruleChoice != 0 || ruleChoice != 1) {
                        System.out.println("Sorry, I don't understand, try again...");
                        ruleChoice = Integer.parseInt(reader.readLine());
                    }
                }
            }
        }
    }

    public int askNumOfPlayers() throws IOException {
        System.out.print("How many players do you want to play with?\n Insert a number between 2 and 4:");
        return Integer.parseInt(reader.readLine());
    }

    public String askPlayerNickname() throws IOException {
        System.out.print("\nChoose a nickname:");
        return reader.readLine();
    }

    public int webProtocol() throws IOException {
        System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
        return Integer.parseInt(reader.readLine());
    }

    public int userInterface() throws IOException {
        System.out.print("""
                Do you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?
                Press 1 for 'TUI', 2 for 'GUI':""");
        return Integer.parseInt(reader.readLine());
    }

    public int askPlayAgain() throws IOException {
        System.out.print("Dou you want to play again?\nPress 0 for 'No', 1 for 'Yes':");
        return Integer.parseInt(reader.readLine());
    }

    public int askColumn() throws IOException {
        System.out.println("Choose the column, a cypher from 0 to 4:");
        return Integer.parseInt(reader.readLine());
    }//TODO in Controller check the input

    //With this function I ask the player the position of the first Tile it intends to choose,
    //if it chooses only one this function returns the coordinates of that tile
    public int[][] askFirstTilePosition() throws IOException {
        System.out.println("Choose the column, a cypher from 0 to 8:");
        int boardColumn = Integer.parseInt(reader.readLine());
        while(boardColumn<0 || boardColumn>8) {
            boardColumn = Integer.parseInt(reader.readLine());
        }
            System.out.println("Choose the row, a cypher from 0 to 8:");
            int boardRow = Integer.parseInt(reader.readLine());
            while(boardRow < 0 || boardRow > 8) {
                boardRow = Integer.parseInt(reader.readLine());}
        return new int[][]{{boardColumn}, {boardRow}};
    }//TODO in Controller check the input
    public int askNumberOfChosenTiles() throws IOException {
        System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
        return Integer.parseInt(reader.readLine());
    }//TODO in Controller check the input

    public int askDirection() throws IOException {
        System.out.println("In which direction do you want to pick the tiles?\nChoose one in between Up (0), Down(1), Left(2), Right(3):");
        return Integer.parseInt(reader.readLine());
    }//TODO in Controller check the input

    public void printTilesInHand(ArrayList<Tile> tilesInHand){
        System.out.print(tilesInHand);
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert one tile
    public int askTileToInsert() throws IOException {
        System.out.println("""
                Now you have to choose the disposition of the chosen tiles.
                The first one on the left has an index 0, and so on...
                Digit a number (the index of the tile you want to put into the bookshelf):""");
        return Integer.parseInt(reader.readLine());
    }//TODO in Controller check the input


}
