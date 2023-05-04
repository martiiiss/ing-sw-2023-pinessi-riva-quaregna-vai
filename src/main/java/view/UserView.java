package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class UserView implements Serializable {
    private static final long serialVersionUID = -23874204704L;

    public int askNumOfPlayer() throws IOException {
        System.out.println("Insert num of player: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int numOfPlayer = Integer.parseInt(reader.readLine());
        return numOfPlayer;
    }
}
