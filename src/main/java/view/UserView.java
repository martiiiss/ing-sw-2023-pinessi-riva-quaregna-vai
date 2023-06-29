package view;

import distributed.messages.Message;
import model.*;
import org.jetbrains.annotations.NotNull;
import util.Observable;
import java.io.*;
import java.util.ArrayList;
import static util.Event.*;

/**Class that represents the Textual User Interface*/
public class UserView extends Observable implements Serializable {
    @Serial
    private static final long serialVersionUID = -2387426539475804704L;

    /**
     * <p>
     *     Method used to ask the first player number of players it wants to play with.<br>
     *     If the input from the player is invalid a {@link NumberFormatException} gets caught.
     *     This method also notifies the observers with a {@link Message} if everything worked.
     * </p>
     * @return an int that represents the max number of players for a match, in case of error -1
     * @throws RuntimeException if a {@code IOException} gets caught
     */
    public int askNumOfPlayer() throws IOException {
        int numOfPlayer = 0;
        System.out.println("Insert num of player: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try{
            int numOfPlayers = Integer.parseInt(reader.readLine());
            notifyObservers(o -> {
                try {
                    o.onUpdate(new Message(numOfPlayer, ASK_NUM_PLAYERS));
                } catch (IOException | NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            });
            return numOfPlayers;
        }catch(NumberFormatException e) {
        }
        return -1;
    }

    /**
     * <p>
     *     Method used to ask the player its nickname.<br>
     *     This method notifies the observers if everything worked.
     * </p>
     * @return a {@code String} that represents the nickname
     * @throws RuntimeException if a {@code IOException} gets caught
     * @throws IllegalArgumentException if the player chooses an inappropriate parameter*/
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

/**
 * <p>
 *     Method used to ask a player which type of interface he prefers.<br>
 *     If the player chose a wrong parameter a {@link NumberFormatException} or a {@link IllegalArgumentException}
 *     will be catch.
 *     This method also notifies the observers with a {@link Message} if everything worked.
 *
 * </p>
 * @return an int that represents the choice of the player, in case of error -1
 * @throws RuntimeException if a {@link IOException} gets caught
 */
    public int userInterface() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
            System.out.print("""
                    Do you prefer a Terminal User Interface (TUI) or a Graphical User Interface (GUI)?
                    Press 1 for 'TUI', 2 for 'GUI':""");
            try{
                int userInterface =  Integer.parseInt(reader.readLine());
                notifyObservers(o -> {
                    try {
                        o.onUpdate(new Message(userInterface, CHOOSE_VIEW));
                    } catch (IOException | NumberFormatException e) {
                    }
                });
                return userInterface;
        } catch (IllegalArgumentException | IOException e) {
                System.out.println();
        }
        return -1;
    }

    /**
     * <p>
     *     Method that shows the player the {@code Board}. <br>
     * </p>
     * @param board the Board of a match*/
    public void showTUIBoard(@NotNull Board board) {
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

    /**
     * <p>
     *     Method that shows the player the {@code Bookshelf}. <br>
     * </p>
     * @param bookshelf a specific Bookshelf*/
    public void showTUIBookshelf(@NotNull Bookshelf bookshelf) {
        Tile[][] tilesInBookshelf = bookshelf.getBookshelf();
        System.out.println("╔═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 6; i++) {
            if(i>0)
                System.out.println("\n╠═══╬═══╬═══╬═══╬═══╣");
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

    /**
     * <p>
     *     Method that asks the player the column in which it wants to put the tiles.<br>
     *     If the player chose a wrong parameter a {@link IllegalArgumentException}
     *     will be catch.
     *     This method also notifies the observers with a {@link Message} if everything worked.
     * </p>
     * @return an int that represents the index of the chosen column, in case of error -1
     * @throws RuntimeException if a {@link IOException} gets caught*/
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

    /**
     * <p>
     *     Method that makes the player choose the tile on the board.<br>
     *     This method also notifies the observers with a {@link Message} if everything worked.
     * </p>
     * @return a {@code String} of coordinates formed like this: <code>(1,3)</code>
     * @throws RuntimeException if a {@link IOException} gets caught */
    public String askTilePosition() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
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

    /**
     * <p>
     *     Method that asks the player how many tiles it wants to pick from the board.<br>
     *     If the player chose a wrong parameter a {@link IllegalArgumentException}
     *     will be catch.
     *     This method also notifies the observers with a {@link Message} if everything worked.
     * </p>
     * @return an int that represents the number of tiles that the player wants to pick up from the board, in case of error -1
     * @throws RuntimeException if a {@link IOException} gets caught*/
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

    /**
     * <p>
     *     Method that prints the tiles that a player picked.<br>
     * </p>
     * @param tilesInHand an {@code ArrayList} of {@code Tiles}
     * that represents the tiles a player has picked from the board */
    public void printTilesInHand(@NotNull ArrayList<Tile> tilesInHand) {
        System.out.println("These are the tiles you picked:");
        switch (tilesInHand.size()) {
            case 1 -> System.out.println("╔═1═╗");
            case 2 -> System.out.println("╔═1═╦═2═╗");
            case 3 -> System.out.println("╔═1═╦═2═╦═3═╗");
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
            case 1 -> System.out.println("╚═══╝");
            case 2 -> System.out.println("╚═══╩═══╝");
            case 3 -> System.out.println("╚═══╩═══╩═══╝");
        }
    }

    /**
     * <p>
     *     Method that asks the player the disposition of the tiles. <br>
     *     If the player chose a wrong parameter a {@link NumberFormatException}
     *     will be catch.
     *     This method also notifies the observers with a {@link Message} if everything worked.
     * </p>
     * @param tilesInHand an {@code ArrayList} of {@code Tiles}
     * @return an int that represents the index of a tile from the {@code tilesInHand}
     * @throws RuntimeException if a {@link IOException} gets caught*/
    public int askTileToInsert(ArrayList<Tile> tilesInHand) throws IOException {
        int index = -1;
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
                } catch (NumberFormatException ex) {
                }
            } while (index<1 || index>tilesInHand.size());
            index--;
        }while (tilesInHand.get(index).getType()==Type.NOTHING);
        return index;
    }

    /**
     * <p>
     *     Method that shows a list of actions for an active player.<br>
     *     If the player chose a wrong parameter a {@link NumberFormatException}
     *     will be catch.
     * </p>
     * @return an int that represents the action that the player, in case of error -1*/
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
        }catch (NumberFormatException e) {
        }
        return -1;
    }

    /**
     * <p>
     *     Method that shows the textual rule for the common goal cards that have been selected.<br>
     * </p>
     * @param commonGoalCards an {@code ArrayList} of {@code CommonGoalCard} that represents the two chosen common goal cards*/
    public void showCGC(@NotNull ArrayList<CommonGoalCard> commonGoalCards) {
        int id;
        for (CommonGoalCard commonGoalCard : commonGoalCards){
            printScoringToken(commonGoalCard);
            id = commonGoalCard.getIdCGC();
            System.out.println("\u001B[35m");
            switch (id) {
                case 1 -> System.out.println("""
                        Six groups each containing at least
                        2 tiles of the same type (not necessarily
                        in the depicted shape).
                        The tiles of one group can be different
                        from those of another group.""");

                case 2 -> System.out.println("Five tiles of the same type forming a\n" +
                            "diagonal. ");

                case 3 -> System.out.println("""
                        Four groups each containing at least
                        4 tiles of the same type (not necessarily
                        in the depicted shape).
                        The tiles of one group can be different
                        from those of another group.""");

                case 4 -> System.out.println("""
                        Four lines each formed by 5 tiles of
                        maximum three different types. One
                        line can show the same or a different
                        combination of another line.""");

                case 5 -> System.out.println("Four tiles of the same type in the four\n" +
                            "corners of the bookshelf.");

                case 6 -> System.out.println("Two columns each formed by 6\n" +
                            "different types of tiles.");

                case 7 -> System.out.println("""
                        Two groups each containing 4 tiles of
                        the same type in a 2x2 square. The tiles
                        of one square can be different from
                        those of the other square.""");

                case 8 -> System.out.println("""
                        Two lines each formed by 5 different
                        types of tiles. One line can show the
                        same or a different combination of the
                        other line.""");

                case 9 -> System.out.println("""
                        Three columns each formed by 6 tiles\s
                        of maximum three different types. One
                        column can show the same or a different
                        combination of another column""");

                case 10 -> System.out.println("Five tiles of the same type forming an X.");

                case 11 -> System.out.println("""
                        Eight tiles of the same type. There’s no
                        restriction about the position of these
                        tiles.""");

                case 12 -> System.out.println("""
                        Five columns of increasing or decreasing
                        height. Starting from the first column on
                        the left or on the right, each next column
                        must be made of exactly one more tile.
                        Tiles can be of any type.""");
            }
            System.out.print("\u001B[0m");
        }
    }

    /**
     * <p>
     *     Method used to print the scoring token.<br>
     * </p>
     * @param commonGoalCard the {@code CommonGoalCard} to which the scoring token refers*/
    private void printScoringToken(@NotNull CommonGoalCard commonGoalCard) {
        if(commonGoalCard.getTokenStack().size()==0) {
            System.out.println("\u001B[35mThere are no more Scoring Tokens for this Common Goal Card...");
        }
        System.out.println("\u001B[35mScoring Token of Common Goal Card #"+commonGoalCard.getIdCGC());
        if(commonGoalCard.getTokenStack().size()==0){
            System.out.println("0"+"\u001B[0m");
        } else{
            System.out.println(commonGoalCard.getTokenStack().get(commonGoalCard.getTokenStack().size()-1).getValue()+"\u001B[0m");
        }
    }

    /**
     * <p>
     *     Method that prints the personal goal card of a player.<br>
     * </p>
     * @param personalGoalCard the {@code PersonalGoalCard} associated to a player*/
    public void showPGC(PersonalGoalCard personalGoalCard) {
        Tile[][] pgc = personalGoalCard.getPGC();
        System.out.println("╔═══╦═══╦═══╦═══╦═══╗");
        for (int i = 0; i < 6; i++) {
            if(i>0)
                System.out.println("\n╠═══╬═══╬═══╬═══╬═══╣");
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

    /**
     * <p>
     *     Method that prints the lit of players for a match.<br>
     * </p>
     * @param listOfPlayers an {@code ArrayList} of {@code Players} that represents the players in a match*/
    public void showPlayers(@NotNull ArrayList<Player> listOfPlayers) {
        int i = 1;
        System.out.println("Here's the list of Players!\nEach one with their score");
        for(Player player : listOfPlayers) {
            System.out.println(" "+i+") \u001B[36m"+player.getNickname()+"\u001B[0m SCORE: \u001B[36m"+player.getScore()+"\u001B[0m ");
            i++;
        }
    }

    /**
     * <p>
     *     Method that shows a list of actions for a passive player.<br>
     * </p>*/
    public void askPassiveAction()  {
        System.out.println("1) Look at the Board");
        System.out.println("2) Check the CommonGoalCards");
        System.out.println("3) Check your PersonalGoalCard");
        System.out.println("4) Look at other player's Bookshelves");
        System.out.println("5) Open the chat");
        System.out.println("6) Show the player list");
    }

    /**
     * <p>
     *     Method that waits for an input by the player.<br>
     *     If the player chose a wrong parameter a {@link NumberFormatException} o a {@link IOException}
     *     will be catch.
     * </p>
     * @return an int that represents the input from the player, in case of error -1*/
    public int waitInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException | IOException ignored) {}
        return -1;
    }

    /**
     * <p>
     *     Method that prints the final rank of a match.<br>
     * </p>
     * @param listOfPlayers an {@code ArrayList} of {@code Player} that represents the final rank of a match*/
    public void gameOver(ArrayList<Player> listOfPlayers) {
        System.out.println("\n\n\n\n\n<GAME OVER>");
        System.out.println("FINAL SCOREBOARD:");
        showPlayers(listOfPlayers);
    }
}
