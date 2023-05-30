package view;

import distributed.messages.Message;
import model.*;
import util.Cord;
import util.Observable;
import util.Observer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Spliterator;

import static util.Event.*;

public class UserView extends Observable implements Serializable, ViewInterface {
    private static final long serialVersionUID = -23874204704L;



    public int askNumOfPlayer() throws IOException {
        int numOfPlayer = 0;
        System.out.println("Insert num of player: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try{
            int numOfPlayers = Integer.parseInt(reader.readLine());
            notifyObservers(o -> {
                try {
                    o.onUpdate(new Message(numOfPlayer, ASK_NUM_PLAYERS));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return numOfPlayers;
        }catch(NumberFormatException e) {}
        return -1;
    }

    public String askPlayerNickname() throws IOException {
        String nickname;
        do {
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
            try {
                System.out.print("\nChoose a nickname:");
                nickname =  reader.readLine();
                String finalNickname = nickname;
                notifyObservers(o -> {
                    try {
                        o.onUpdate(new Message(finalNickname, SET_NICKNAME));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid input!");
                throw new IllegalArgumentException();
            }
        }while (nickname.isEmpty());
        return nickname;
    }


    public int webProtocol() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.print("\nChoose a communication protocol, \ndigit 1 for 'Socket', 2 for 'JavaRMI':");
            try {
                int webProtocol = Integer.parseInt(reader.readLine());
                notifyObservers(o -> {
                    try {
                        o.onUpdate(new Message(webProtocol, CHOOSE_NETWORK_PROTOCOL));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return webProtocol;
            }catch(NumberFormatException e) {}
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
            try {
                int userInterface =  Integer.parseInt(reader.readLine());
                notifyObservers(o -> {
                    try {
                        o.onUpdate(new Message(userInterface, CHOOSE_VIEW));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                return userInterface;
            }catch(NumberFormatException e) {}
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }

    public void showTUIBoard(Board board) {
        Tile[][] tilesOnBoard = board.getBoard();
        System.out.println("┌─0─┬─1─┬─2─┬─3─┬─4─┬─5─┬─6─┬─7─┬─8─┐");
        for (int i = 0; i < board.BOARD_ROW; i++) {
            System.out.print(i);
            for (int j = 0; j < board.BOARD_COLUMN; j++) {
                switch (tilesOnBoard[i][j].getType()) {
                    case BLOCKED -> System.out.print("\033[31;100;1m   \u001B[0m");
                    case NOTHING -> System.out.print("\033[37;100;1m   \u001B[0m");
                    case CAT -> System.out.print("\033[37;42;1m   \u001B[0m");
                    case BOOK -> System.out.print("\033[37;107;1m   \u001B[0m");
                    case FRAME -> System.out.print("\033[37;44;1m   \u001B[0m");
                    case GAME -> System.out.print("\033[37;43;1m   \u001B[0m");
                    case PLANT -> System.out.print("\033[37;45;1m   \u001B[0m");
                    case TROPHY -> System.out.print("\033[37;46;1m   \u001B[0m");
                }
                System.out.print("│");
            }
            if(i!= board.BOARD_ROW-1)
                System.out.println("\n├───┼───┼───┼───┼───┼───┼───┼───┼───┤");
        }
        System.out.println("\n└───┴───┴───┴───┴───┴───┴───┴───┴───┘");
        System.out.println();
    }
    public void showTUIBookshelf(Bookshelf bookshelf) {
        Tile[][] tilesInBookshelf = bookshelf.getBookshelf();
        System.out.println("╔═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 6; i++) {
            if(i>0 && i<=5)
                System.out.println("\n╠═══╬═══╬═══╬═══╬═══╣");
            //if(i==5)
               // System.out.println();

            for (int j = 0; j < 5; j++) {
                if(j==0)
                    System.out.print("║");
                switch (tilesInBookshelf[i][j].getType()) {
                    case NOTHING -> System.out.print("\033[37;100;1m   \u001B[0m");
                    case CAT -> System.out.print("\033[37;42;1m   \u001B[0m");
                    case BOOK -> System.out.print("\033[37;107;1m   \u001B[0m");
                    case FRAME -> System.out.print("\033[37;44;1m   \u001B[0m");
                    case GAME -> System.out.print("\033[37;43;1m   \u001B[0m");
                    case PLANT -> System.out.print("\033[37;45;1m   \u001B[0m");
                    case TROPHY -> System.out.print("\033[37;46;1m   \u001B[0m");
                }
                System.out.print("║");
            }
        }
        System.out.println("\n╠═══╩═══╩═══╩═══╩═══╣");
        System.out.println("║ 0   1   2   3   4 ║");
        System.out.println("╚═══════════════════╝");
    }
    public int askColumn() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            System.out.println("Choose the column, an integer from 0 to 4:");
            int chosenColumn = Integer.parseInt(reader.readLine());
            notifyObservers(o -> {
                try {
                    o.onUpdate(new Message(chosenColumn, TURN_COLUMN));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return chosenColumn;
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
        String finalIn = in;
        notifyObservers(o -> {
            try {
                o.onUpdate(new Message(finalIn, TURN_POSITION));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
            int numOfChosenTiles =  Integer.parseInt(reader.readLine());
            notifyObservers(o -> {
                try {
                    o.onUpdate(new Message(numOfChosenTiles, TURN_AMOUNT));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return numOfChosenTiles;
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Invalid input!");
        }
        return -1;
    }
    public void printTilesInHand(ArrayList<Tile> tilesInHand) {
        System.out.println("These are the tiles you picked:");
        switch (tilesInHand.size()) {
            case 1 -> {
                System.out.println("╔═1═╗");
                break;
            }
            case 2 -> {
                System.out.println("╔═1═╦═2═╗");
                break;
            }
            case 3 -> {
                System.out.println("╔═1═╦═2═╦═3═╗");
                break;
            }
        }
        System.out.print("║");
        for (Tile tile : tilesInHand) {
            switch (tile.getType()) {
                case NOTHING -> System.out.print("\033[37;100;1m   \u001B[0m");
                case CAT -> System.out.print("\033[37;42;1m   \u001B[0m");
                case BOOK -> System.out.print("\033[37;107;1m   \u001B[0m");
                case FRAME -> System.out.print("\033[37;44;1m   \u001B[0m");
                case GAME -> System.out.print("\033[37;43;1m   \u001B[0m");
                case PLANT -> System.out.print("\033[37;45;1m   \u001B[0m");
                case TROPHY -> System.out.print("\033[37;46;1m   \u001B[0m");
            }
            System.out.print("║");
        }
        System.out.println();

        switch (tilesInHand.size()) {
            case 1 -> {
                System.out.println("╚═══╝");
                break;
            }
            case 2 -> {
                System.out.println("╚═══╩═══╝");
                break;
            }
            case 3 -> {
                System.out.println("╚═══╩═══╩═══╝");
                break;
            }
        }
    }

    //This method asks the index of the tile to insert -> print the tiles in hand every time the player puts the tile into the bookshelf
    //I insert one tile
    public int askTileToInsert(ArrayList<Tile> tilesInHand) throws IOException {
        int index = -1;
        boolean valid;
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        do {
            do {
                System.out.println("You have to choose the disposition of the chosen tiles...");
                printTilesInHand(tilesInHand);
                System.out.print("Type the index of the one you wish to insert first:");
                try {
                    index = Integer.parseInt(reader.readLine());
                    int finalIndex = index;
                    notifyObservers(o -> {
                        try {
                            o.onUpdate(new Message(finalIndex, TURN_PICKED_TILES));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }catch (NumberFormatException ex) {}
            }while (index<1 || index>tilesInHand.size());
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
        System.out.println("7) Show the player list");
        try {
            return Integer.parseInt(reader.readLine());
        }catch (NumberFormatException e) {}
        return -1;
    }
    public void showCGC(ArrayList<CommonGoalCard> commonGoalCards) {
        int id;
        for (CommonGoalCard commonGoalCard : commonGoalCards){
            printScoringToken(commonGoalCard);
            id = commonGoalCard.getIdCGC();
            System.out.println("\u001B[35m");
            switch (id) {
                case 1 -> {
                    System.out.println("Six groups each containing at least\n" +
                            "2 tiles of the same type (not necessarily\n" +
                            "in the depicted shape).\n" +
                            "The tiles of one group can be different\n" +
                            "from those of another group.");
                }
                case 2 -> {
                    System.out.println("Five tiles of the same type forming a\n" +
                            "diagonal. ");
                }
                case 3 -> {
                    System.out.println("Four groups each containing at least\n" +
                            "4 tiles of the same type (not necessarily\n" +
                            "in the depicted shape).\n" +
                            "The tiles of one group can be different\n" +
                            "from those of another group.");
                }
                case 4 -> {
                    System.out.println("Four lines each formed by 5 tiles of\n" +
                            "maximum three different types. One\n" +
                            "line can show the same or a different\n" +
                            "combination of another line.");
                }
                case 5 -> {
                    System.out.println("Four tiles of the same type in the four\n" +
                            "corners of the bookshelf.");
                }
                case 6 -> {
                    System.out.println("Two columns each formed by 6\n" +
                            "different types of tiles.");
                }
                case 7 -> {
                    System.out.println("Two groups each containing 4 tiles of\n" +
                            "the same type in a 2x2 square. The tiles\n" +
                            "of one square can be different from\n" +
                            "those of the other square.");
                }
                case 8 -> {
                    System.out.println("Two lines each formed by 5 different\n" +
                            "types of tiles. One line can show the\n" +
                            "same or a different combination of the\n" +
                            "other line.");
                }
                case 9 -> {
                    System.out.println("Three columns each formed by 6 tiles \n" +
                            "of maximum three different types. One\n" +
                            "column can show the same or a different\n" +
                            "combination of another column");
                }
                case 10 -> {
                    System.out.println("Five tiles of the same type forming an X.");
                }
                case 11 -> {
                    System.out.println("Eight tiles of the same type. There’s no\n" +
                            "restriction about the position of these\n" +
                            "tiles.");
                }
                case 12 -> {
                    System.out.println("Five columns of increasing or decreasing\n" +
                            "height. Starting from the first column on\n" +
                            "the left or on the right, each next column\n" +
                            "must be made of exactly one more tile.\n" +
                            "Tiles can be of any type.");
                }
            }
            System.out.print("\u001B[0m");
        }
    }
    private void printScoringToken(CommonGoalCard commonGoalCard) {
        if(commonGoalCard.getTokenStack().size()==0) {
            System.out.println("\u001B[35mThere are no more Scoring Tokens for this Common Goal Card...");
        }
        System.out.println("\u001B[35mScoring Token of Common Goal Card #"+commonGoalCard.getIdCGC());
        System.out.println(commonGoalCard.getTokenStack().get(commonGoalCard.getTokenStack().size()-1).getValue()+"\u001B[0m");
    }

    public void showPGC(PersonalGoalCard personalGoalCard) {
        Tile[][] pgc = personalGoalCard.getPGC();
        System.out.println("╔═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 6; i++) {
            if(i>0 && i<=5)
                System.out.println("\n╠═══╬═══╬═══╬═══╬═══╣");
            //if(i==5)
            // System.out.println();

            for (int j = 0; j < 5; j++) {
                if(j==0)
                    System.out.print("║");
                switch (pgc[i][j].getType()) {
                    case NOTHING -> System.out.print("\033[37;100;1m   \u001B[0m");
                    case CAT -> System.out.print("\033[37;42;1m   \u001B[0m");
                    case BOOK -> System.out.print("\033[37;107;1m   \u001B[0m");
                    case FRAME -> System.out.print("\033[37;44;1m   \u001B[0m");
                    case GAME -> System.out.print("\033[37;43;1m   \u001B[0m");
                    case PLANT -> System.out.print("\033[37;45;1m   \u001B[0m");
                    case TROPHY -> System.out.print("\033[37;46;1m   \u001B[0m");
                }
                System.out.print("║");
            }
        }
        System.out.println("\n╠═══╩═══╩═══╩═══╩═══╣");
        System.out.println("║      YourPGC      ║");
        System.out.println("╚═══════════════════╝");
    }

    public void chatOptions(Player player) {
        System.err.println("\uD83D\uDDEA NON ANCORA IMPLEMENTATO :P");
    }
    public void showPlayers(ArrayList<Player> listOfPlayers) {
        int i = 1;
        System.out.println("Here's the list of Players!\nEach one with their score");
        for(Player player : listOfPlayers) {
            System.out.println(" "+i+") \u001B[36m"+player.getNickname()+"\u001B[0m SCORE: \u001B[36m"+player.getScore()+"\u001B[0m ");
            i++;
        }
    }
    public int askPassiveAction() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("1) Look at the Board");
        System.out.println("2) Check the CommonGoalCards");
        System.out.println("3) Check your PersonalGoalCard");
        System.out.println("4) Look at other player's Bookshelves");
        System.out.println("5) Open the chat");
        System.out.println("6) Show the player list");
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
        }
        return -1;
    }
}
