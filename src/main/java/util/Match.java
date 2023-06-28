package util;

import controller.Controller;
import distributed.ClientInterface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**Class that represents a single match*/
public class Match implements Serializable {
    private final List<ClientInterface> clientsConnected;
    private final Controller gameController;
    private int maxSize = 5;
    private boolean clientDisconnected = false;

    /**
     * Constructor of the Class. This saves the clients connected in an {@code ArrayList}
     * and also creates a Controller instance for this match. */
    public Match() {
        this.clientsConnected = new ArrayList<>();
        gameController = new Controller();
    }

    /**
     * Method used to add a player to a match.<br>
     * @param client is the {@link ClientInterface}
     * that has to be added to the list of clients connected to a match */
    public void addPlayer(ClientInterface client) {
        clientsConnected.add(client);
    }

    /**
     * Method that returns a {@code List} of clients connected to a match.<br>
     * @return {@code List} of {@link ClientInterface} that represents the client connected to a match*/
    public List<ClientInterface> getListOfClients() {
        return this.clientsConnected;
    }

    /**
     * Method that returns the Controller of a match.<br>
     * @return the {@link Controller} of a match*/
    public Controller getGameController() {
        return this.gameController;
    }

    /**
     * Method used to set the maximum number of players for a match.<br>
     * @param maxSize is an int that represents the maximum number of players for a match<br>
     * <small>(chosen by the first player)</small>*/
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Method that returns the maximum number of players for a match.<br>
     * @return an int that represents the maximum number of players for a match */
    public int getMaxSize() {
        return this.maxSize;
    }

    /**
     * Method that sets to true the connection of a client.<br>
     * */
    public void setClientsConnected() {
        this.clientDisconnected = true;
    }

    /**
     * Method that checks if a client is connected.<br>
     * @return <b>true</b> if a client is connected, <b>false</b> otherwise*/
    public boolean getClientDisconnected() {
        return this.clientDisconnected;
    }
}
