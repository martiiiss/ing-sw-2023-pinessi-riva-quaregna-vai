package view;

import model.Tile;
import util.Cord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

public class UserView implements Serializable {
    private static final long serialVersionUID = -8919862611033094284L;
    BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
    public int askNumOfPlayer() throws IOException {
        int numOfPlayer = 0;
        System.out.println("Insert num of player: ");
        try {
             numOfPlayer =  Integer.parseInt(reader.readLine());
        }catch (NumberFormatException exception) { System.err.println("Invalid format...");}
        return numOfPlayer;
    } //implementare -> controllare in controller che non venga accettato lo 0

    public String askPlayerNickname() throws IOException {
        System.out.print("\nChoose a nickname:");
        String chosenNickname = null;
        try {
            chosenNickname = reader.readLine();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input!");
            throw new IllegalArgumentException();
        }
        return chosenNickname;
    } //implementare -> controllare in controller che non venga accettato null

    public int communicationProtocol() {
        System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
        int chosenProtocol = 0;
        try {
            chosenProtocol = Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return chosenProtocol;
    }//implementare -> controllare in controller che non venga accettato lo 0

    public int userInterface() {
        System.out.print("""
                    Do you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?
                    Press 1 for 'TUI', 2 for 'GUI':""");
        int chosenInterface = 0;
        try {
           chosenInterface = Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return chosenInterface;
    }//implementare -> controllare in controller che non venga accettato lo 0

    public int askNumberOfChosenTiles() throws IOException {
        System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
        int numberOfChosenTiles = 0;
        try {
            numberOfChosenTiles = Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return numberOfChosenTiles;
    }// implementare -> fare i vari controlli seguendo l'MVC

    public String askTilePosition() throws IOException {
        Cord cord = new Cord();
        System.out.print("Input your coordinates as 2 integers separated by a comma (tiles must be adjacent):");
        String in = reader.readLine();
        while (in.isEmpty()) {
            System.err.println("Empty, try again");
            in = reader.readLine();
        }
        return in;
    } //implementare -> fare gli adeguati controlli in controller

    public int askColumn() {
        System.out.println("Choose the column, an integer from 0 to 4:");
        int chosenColumn = -1;
            try {
                chosenColumn = Integer.parseInt(reader.readLine());
            } catch (IllegalArgumentException | IOException e) {
                System.err.println("Invalid input!");
            }
        return chosenColumn;
    } //implementare -> controllare in controller che non venga accettato lo 0

    public void printTilesInHand(ArrayList<Tile> tilesInHand) {
        System.out.print(tilesInHand);
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert the tiles one at a time
    public int askTileToInsert(ArrayList<Tile> tilesInHand) {
        System.out.println("""
                    Now you have to choose the disposition of the chosen tiles.
                    The first one on the left has an index 0, and so on...
                    Digit a number (the index of the tile you want to put into the bookshelf):""");
        int tileToInsert = -1;
        try {
            for (Tile tile : tilesInHand) {
                System.out.println("These are the tiles you picked: " + tile.getType());
            }
            tileToInsert = Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return tileToInsert;
    }//TODO stampare al tizio il numero accanto alla tile che vuole
    //implementare -> controllare che quello che vuole inserire esista

}
