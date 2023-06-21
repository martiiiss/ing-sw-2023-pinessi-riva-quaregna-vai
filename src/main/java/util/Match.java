package util;

import controller.Controller;
import distributed.Client;
import distributed.RMI.ClientInterface;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private long serialVersionUID = -8672464204670634699L;
    private List<ClientInterface> clientsConnected;
    private Controller gameController;
    private int maxSize = 5;

    public Match() throws IOException {
        this.clientsConnected = new ArrayList<>();
        gameController = new Controller();
    }

    public void addPlayer(ClientInterface client) {
        clientsConnected.add(client);
    }
    public List<ClientInterface> getListOfClients() {
        return this.clientsConnected;
    }
    public Controller getGameController() {
        return this.gameController;
    }
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    public int getMaxSize() {
        return this.maxSize;
    }
}
