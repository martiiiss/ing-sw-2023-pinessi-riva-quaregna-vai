package view;

import model.*;
import util.Cord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Spliterator;

public class UserView implements Serializable {
    private static final long serialVersionUID = -23874204704L;

    public int askNumOfPlayer() throws IOException {
        int numOfPlayer = 0;
        System.out.println("Insert num of player: ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
            numOfPlayer = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException exception) {
            System.err.println("Invalid format...");
        }
        return numOfPlayer;
    }

    public String askPlayerNickname() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.print("\nChoose a nickname:");
            return reader.readLine();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input!");
            throw new IllegalArgumentException();
        } //TODO devo gestire l'eccezione che lancia!!! perchè deve poter richiedere !
    }


    public int webProtocol() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }


    public int userInterface() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.print("""
                    Do you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?
                    Press 1 for 'TUI', 2 for 'GUI':""");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    public void showTUIBoard(Board board) {
        Tile[][] tilesOnBoard = board.getBoard();
        System.out.print("   0  1  2  3  4  5  6  7  8");
        for (int i = 0; i < board.BOARD_ROW; i++) {
            System.out.println();
            System.out.print(+i+" ");
            for (int j = 0; j < board.BOARD_COLUMN; j++) {
                switch (tilesOnBoard[i][j].getType()) {
                    case BLOCKED -> System.out.print("\033[31;47;1m   \u001B[0m");
                    case CAT -> System.out.print("\u001B[32m □ \u001B[0m");
                    case BOOK -> System.out.print("\u001B[97m □ \u001B[0m");
                    case FRAME -> System.out.print("\u001B[34m □ \u001B[0m");
                    case GAME -> System.out.print("\u001B[33m □ \u001B[0m");
                    case PLANT -> System.out.print("\u001B[35m □ \u001B[0m");
                    case TROPHY -> System.out.print("\u001B[36m □ \u001B[0m");
                    case NOTHING -> System.out.print("\033[31;40;0m   \u001B[0m");
                }
            }
        }
        System.out.println();
    }
    public void showTUIBookshelf(Bookshelf bookshelf) {
        System.out.println("Here's the player in turn's bookshelf:");
        Tile[][] tilesInBookshelf = bookshelf.getBookshelf();
        for (int i = 0; i < 6; i++) {
            System.out.println("+---------------+");
            for (int j = 0; j < 5; j++) {
                if (j == 0) System.out.print("|");
                switch (tilesInBookshelf[i][j].getType()) {
                    case NOTHING -> System.out.print("\u001B[90m □ \u001B[0m");
                    case CAT -> System.out.print("\u001B[32m □ \u001B[0m");
                    case BOOK -> System.out.print("\u001B[97m □ \u001B[0m");
                    case FRAME -> System.out.print("\u001B[34m □ \u001B[0m");
                    case GAME -> System.out.print("\u001B[33m □ \u001B[0m");
                    case PLANT -> System.out.print("\u001B[35m □ \u001B[0m");
                    case TROPHY -> System.out.print("\u001B[36m □ \u001B[0m");
                }
                if (j == 4) System.out.println("|");
            }
        }
    }
    public int askColumn() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.println("Choose the column, an integer from 0 to 4:");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    public String askTilePosition() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        Cord cord = new Cord();
        System.out.print("Input your coordinates as 2 integers separated by a comma\n(tiles must be adjacent):");
        String in = reader.readLine();
        while (in.isEmpty()) {
            System.err.println("Empty, try again");
            in = reader.readLine();
        }
        return in;
    }

    public int askNumberOfChosenTiles() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }
    public void printTilesInHand(ArrayList<Tile> tilesInHand) {
        System.out.println("These are the tiles you picked:");
        switch (tilesInHand.size()) {
            case 1: {
                System.out.println("[ 1 ]");
                break;
            }
            case 2: {
                System.out.println("[ 1  2 ]");
                break;
            }
            case 3: {
                System.out.println("[ 1  2  3 ]");
                break;
            }
        }
        System.out.print("[");
        for (Tile tile : tilesInHand) {
            switch (tile.getType()) {
                case NOTHING -> System.out.print("\u001B[90m □ \u001B[0m");
                case CAT -> System.out.print("\u001B[32m □ \u001B[0m");
                case BOOK -> System.out.print("\u001B[97m □ \u001B[0m");
                case FRAME -> System.out.print("\u001B[34m □ \u001B[0m");
                case GAME -> System.out.print("\u001B[33m □ \u001B[0m");
                case PLANT -> System.out.print("\u001B[35m □ \u001B[0m");
                case TROPHY -> System.out.print("\u001B[36m □ \u001B[0m");
            }
        }
        System.out.println("]");
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert one tile
    public int askTileToInsert(ArrayList<Tile> tilesInHand) throws IOException {
        int index = -1;
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        do {
            System.out.println("You have to choose the disposition of the chosen tiles...");
            printTilesInHand(tilesInHand);
            System.out.print("Type the index of the one you wish to insert first:");
            index = Integer.parseInt(reader.readLine());
            index--;
        }while (tilesInHand.get(index).getType()==Type.NOTHING);
        return index;
    }
    public int askAction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("1) Look at the Board");
        System.out.println("2) Check the CommonGoalCards");
        System.out.println("3) Check your PersonalGoalCard");
        System.out.println("4) Look at other player's Bookshelves");
        System.out.println("5) Open the chat");
        System.out.println("6) Continue with my turn");
        try {
            return Integer.parseInt(reader.readLine());
        }catch (NumberFormatException exception) {
            exception.printStackTrace();}
        return -1;
    }
    public void showCGC(ArrayList<CommonGoalCard> commonGoalCards) {
        System.err.println("NON ANCORA IMPLEMENTATO :P");
    }

    public void showPGC(Player player) {
        System.err.println("NON ANCORA IMPLEMENTATO :P");
    }

    public void chatOptions(Player player) {
        System.err.println("NON ANCORA IMPLEMENTATO :P");
    }
}
