



package view;
import controller.*;
import model.Tile;
import util.Cord;

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
                //Here will be invoked a magic function that will show the rules :)
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

    public int askNumOfPlayers() {
        try {
            System.out.print("How many players do you want to play with?\n Insert a number between 2 and 4:");
            return Integer.parseInt(reader.readLine());
        } catch (IOException e){
            System.err.println("Invalid input!");
        }
        return -1;
    }//FIXME

    public String askPlayerNickname() throws IOException {
        try{
        System.out.print("\nChoose a nickname:");
            return reader.readLine();
        } catch (IllegalArgumentException e){
            System.err.println("Invalid input!");
            throw new IllegalArgumentException();
        }
    }

    public int webProtocol() throws IOException {
        System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
        return Integer.parseInt(reader.readLine());
    }//FIXME

    public int userInterface() throws IOException {
        System.out.print("""
                Do you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?
                Press 1 for 'TUI', 2 for 'GUI':""");
        return Integer.parseInt(reader.readLine());
    }//FIXME

    public int askPlayAgain() throws IOException {
        System.out.print("Dou you want to play again?\nPress 0 for 'No', 1 for 'Yes':");
        return Integer.parseInt(reader.readLine());
    }//FIXME

    //We ask in which column the player wants to put the tiles
    public int askColumn() throws IOException {
        System.out.println("Choose the column, an integer from 0 to 4:");
        return Integer.parseInt(reader.readLine());
    }//FIXME

    //This method asks the user to input the coordinates of the desired Tile
    //TODO: 1)Check if the tiles are all in the same row/col, Check the input of the user (It must be with the comma)
    public ArrayList<Cord> askTilePosition(int size) throws IOException {
        Cord cord = new Cord();
        ArrayList<Cord> listOfCords = new ArrayList<>();
        for(int i=0; i<size; i++) {
            System.out.print("Input your coordinates as 2 integers separated by a comma:");
            String in = reader.readLine();
            String[]splittedStr = in.split(",");
            cord.setCords(Integer.parseInt(splittedStr[0]),Integer.parseInt(splittedStr[1]));
            listOfCords.add(cord);
        }
        return listOfCords;
    }
    public int askNumberOfChosenTiles() throws IOException {
        System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
        return Integer.parseInt(reader.readLine());
    }//FIXME

    //method that will likely be used in the TUI, we show the player which tiles it had chosen
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
    }//FIXME


}
