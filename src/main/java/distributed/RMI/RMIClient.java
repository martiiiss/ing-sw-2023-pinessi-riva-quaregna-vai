package distributed.RMI;

import distributed.Client;
import model.*;
import util.Error;
import view.UserView;
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import static util.Event.*;

public class RMIClient extends Client implements ClientConnectionRMI, Serializable {
    @Serial
    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private int port;
    private UserView uView = new UserView();
    private Error errorReceived;
    private int myIndex;


    public RMIClient(String address, int port) throws RemoteException {
        super(address, port);
        this.address = address; //server
        this.port = port;
    }

    @Override
    public void startConnection() throws RemoteException, NotBoundException {
        try {
            server = (ServerRMIInterface) Naming.lookup(getUsername());
            this.myIndex = server.initClient(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }  //TODO implement this


    public String getUsername(){ return this.address; }

    public int getPort(){ return this.port; }

    @Override
    public void closeConnection() {
        //TODO implement this
    }

    @Override
    public void disconnected() {

    }
    public void ping(){
        //TODO implement this
    }

    public void disconnect(){
        //TODO implement this
    }
    int numOfPlayers = 0;

    public void lobby() throws IOException { //TODO Metti il sincro per la stampa brutta
        Error errorReceived;
        if(myIndex==0) {//Vuol dire che sono il primo client connesso
            numOfPlayers = uView.askNumOfPlayer();
            while (numOfPlayers<2 || numOfPlayers>4) {
                System.err.println("Retry...");
                numOfPlayers = uView.askNumOfPlayer();
            }
            errorReceived = server.sendMessage(numOfPlayers, ASK_NUM_PLAYERS); //invia al server il numero di giocatori
            System.out.println("Server sent this error: "+errorReceived);
        }
        errorReceived = server.sendMessage(uView.userInterface(),CHOOSE_VIEW); //invia al server la View desiderata
        while (errorReceived!=Error.OK) {
            System.err.println("Invalid value");
            errorReceived = server.sendMessage(uView.userInterface(),CHOOSE_VIEW);
        }
        errorReceived =server.sendMessage(uView.askPlayerNickname(),SET_NICKNAME); //invia al server il nickname
        while (errorReceived!=Error.OK) {
            System.out.println("Server sent this error: " + errorReceived);
            switch (errorReceived) {
                case EMPTY_NICKNAME -> System.err.println("Nickname is empty...");
                case NOT_AVAILABLE -> System.err.println("Nickname already taken...");
            }
            System.out.print("");
            errorReceived = server.sendMessage(uView.askPlayerNickname(), SET_NICKNAME);
        }
        if(myIndex==0){
            errorReceived = server.sendMessage(numOfPlayers,ALL_CONNECTED);
            System.out.println("Server sent this error: " + errorReceived);
            while (errorReceived != Error.OK) {
                errorReceived = server.sendMessage(numOfPlayers, ALL_CONNECTED);
            }
        }
        getModel();
    }

    private Board board;
    private Bookshelf bookshelf;
    private CommonGoalCard commonGoalCard;
    private PersonalGoalCard personalGoalCard;
    private ArrayList<Player> listOfPlayers;
    public void getModel() throws IOException {
        Error errorReceived = server.sendMessage(null,GAME_STARTED);
        while (errorReceived != Error.OK)
             errorReceived = server.sendMessage(null,GAME_STARTED);
        this.board=(Board) server.getModel(GAME_BOARD);
        this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS);

        uView.showTUIBoard(this.board);
        uView.showTUIBookshelf(listOfPlayers.get(0).getMyBookshelf());

    }
}