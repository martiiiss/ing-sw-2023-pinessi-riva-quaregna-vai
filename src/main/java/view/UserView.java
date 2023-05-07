package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class UserView implements Serializable {
    private static final long serialVersionUID = -23874204704L;

    public int askNumOfPlayer() throws IOException {
        int numOfPlayer = 0;
        System.out.println("Insert num of player: ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
            numOfPlayer =  Integer.parseInt(reader.readLine());
        }catch (NumberFormatException exception) { System.err.println("Invalid format...");}
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
        }
    }
}
