package view;

import model.Board;
import model.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class UserView implements Serializable {
    private static final long serialVersionUID = -23874204704L;

    public int askNumOfPlayer(int numOfConnections) throws IOException {
        if(numOfConnections == 1) {
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
        return -1;
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
}
