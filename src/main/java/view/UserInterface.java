package view;

import controller.*;
import distributed.Server;
import model.*;
import util.Cord;
import util.Observable;
import util.Observer;

import java.io.*;
import java.util.ArrayList;


public class UserInterface implements Serializable {
    @Serial
    private static final long serialVersionUID = -38738202973L;
    /*
   Controller cont;
   Game game ;
   Bag bag;
   Board board ;


   public void run() throws IOException {
       cont = new Controller();
       cont.createGame();
       game = cont.getInstanceOfGame();
       bag =  cont.getInstanceOfBag();
       board = cont.getInstanceOfBoard();
   } //this is probably wrong

   public Controller getInstanceOfController(){
       return cont;
   }

    */
    BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

    //Boh, added this one just because
    public void greetings() throws IOException {
        System.out.print("""
                Hi Welcome to MyShelfy!
                Do you want to read the rules?
                Press 1 for 'Yes', 0 for 'No'
                """);
        int ruleChoice = Integer.parseInt(reader.readLine());
        boolean flag = false;
        while (!flag) {
            switch (ruleChoice) {
                case 1 -> {
                    flag = true;
                    System.out.println("Rules...");
                }
                //Here will be invoked a magic function that will show the rules :)
                case 0 -> {
                    flag = true;
                    System.out.println("\nOk, let's go!");
                }
                default -> {
                    while (ruleChoice != 0 || ruleChoice != 1) {
                        System.err.println("Sorry, I don't understand, try again...");
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
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("\nInvalid input!");
        }
        return -1;
    }

    public String askPlayerNickname() throws IOException {
        try {
            System.out.print("\nChoose a nickname:");
            return reader.readLine();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input!");
            throw new IllegalArgumentException();
        }
    }

    public int webProtocol() {
        try {
            System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    public int userInterface() {
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

    public int askPlayAgain() {
        try {
            System.out.print("Dou you want to play again?\nPress 0 for 'No', 1 for 'Yes':");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    //We ask in which column the player wants to put the tiles
    public int askColumn() {
        try {
            System.out.println("Choose the column, an integer from 0 to 4:");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    //This method asks the user to input the coordinates of the desired Tile
    //TODO: 1)Check if the tiles are all in the same row/col, Check the input of the user (It must be with the comma)
    public String askTilePosition() throws IOException {
        Cord cord = new Cord();
        System.out.print("Input your coordinates as 2 integers separated by a comma (tiles must be adjacent):");
        String in = reader.readLine();
        while (in.isEmpty()) {
            System.err.println("Empty, try again");
            in = reader.readLine();
        }
        /*
        try {
            String[] splittedStr = in.split(",");
            cord.setCords(Integer.parseInt(splittedStr[0]), Integer.parseInt(splittedStr[1]));
        } catch (NumberFormatException formatException) {
            if(in.equals("undo")) {
                cont.clearChoice();
                System.out.println("Restoring the board...");
            }
            else
                System.err.println("Invalid format...");
        } catch (ArrayIndexOutOfBoundsException boundsException) {
            System.err.println("Invalid format or non existent coordinate...");
        }
        //showSelectedTiles(cord);

         */
        return in;
    }

    public int askNumberOfChosenTiles() {
        try {
            System.out.println("How many tiles do you want to pick?\nChoose a number between 1 and 3:");
            return Integer.parseInt(reader.readLine());
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    //method that will likely be used in the TUI, we show the player which tiles it had chosen
    public void printTilesInHand(ArrayList<Tile> tilesInHand) {
        for (Tile tile : tilesInHand)
            System.out.println(tile.getType());
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert one tile
    public int askTileToInsert(ArrayList<Tile> tilesInHand) {
        try {
            for (Tile tile : tilesInHand) {
                System.out.println("These are the tiles you picked: " + tile.getType());
            }
            //
            System.out.println("""
                    Now you have to choose the disposition of the chosen tiles.
                    The first one on the left has an index 0, and so on...
                    Digit a number (the index of the tile you want to put into the bookshelf):""");
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

    public void showSelectedTiles(ArrayList<Cord> cords, Board board) {
        Tile[][] tilesOnBoard = board.getBoard();
        Cord cord = new Cord();
        System.out.print("  0  1  2  3  4  5  6  7  8");
        for (int i = 0; i < board.BOARD_ROW; i++) {
            System.out.println();
            System.out.print(+i);
            for (int j = 0; j < board.BOARD_COLUMN; j++) {
                cord.setCords(i,j);
                    System.out.println("\033[31;44;1m □ \u001B[0m");
                    switch (tilesOnBoard[i][j].getType()) {
                        case NOTHING, BLOCKED -> System.out.print("\u001B[90m □ \u001B[0m");
                        case CAT -> System.out.print("\u001B[32m □ \u001B[0m");
                        case BOOK -> System.out.print("\u001B[97m □ \u001B[0m");
                        case FRAME -> System.out.print("\u001B[34m □ \u001B[0m");
                        case GAME -> System.out.print("\u001B[33m □ \u001B[0m");
                        case PLANT -> System.out.print("\u001B[35m □ \u001B[0m");
                        case TROPHY -> System.out.print("\u001B[36m □ \u001B[0m");
                }
            }
        }
        System.out.println();
    }

}

