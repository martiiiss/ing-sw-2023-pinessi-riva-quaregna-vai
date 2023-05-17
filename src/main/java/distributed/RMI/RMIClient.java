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
import java.util.Stack;

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
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private ArrayList<Player> listOfPlayers;
    private Stack<ScoringToken> tokenStack;
    private Player playerInTurn;
    private int indexOfPIT;
    public void getModel() throws IOException {
        Error errorReceived = server.sendMessage(null,GAME_STARTED);
        while (errorReceived != Error.OK)
             errorReceived = server.sendMessage(null,GAME_STARTED);
        this.board =(Board) server.getModel(GAME_BOARD,myIndex);
        this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex);
        this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(GAME_CGC,myIndex);
        this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(GAME_PGC, myIndex);
        this.indexOfPIT = (int) server.getModel(GAME_PIT,myIndex); //This variable/attribute can be used to check if this client is the player in turn (if so he has to make moves on the board)
        this.playerInTurn = listOfPlayers.get(indexOfPIT);
        System.out.println("It's "+playerInTurn.getNickname()+"'s turn!");
        if(myIndex==indexOfPIT) {
            activePlay();
        }
        else passivePlay();
    }
    private void activePlay() throws IOException {
        int choice = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("IT'S YOUR TURN :D");
        System.out.println("Would you like to do any of these actions before making your move?");
        do {
            choice = uView.askAction();
            switch (choice) {
                case 1 -> {
                    System.out.println("Here's the game board...");
                    uView.showTUIBoard(board);
                }
                case 2 -> {
                    System.out.println("Here are the CommonGoalCards...");
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    uView.showPGC(listOfPlayers.get(myIndex));
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    for (Player player : listOfPlayers) {
                        if (listOfPlayers.indexOf(player) == myIndex)
                            System.out.println("This is yours....");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                default -> System.err.println("Invalid value...");
            }
            if(choice!=6)
                System.out.println("What else would you like to do?");
        }while (choice!=6);
        System.out.println("Here's your Bookshelf:");
        uView.showTUIBookshelf(listOfPlayers.get(indexOfPIT).getMyBookshelf());
        uView.showTUIBoard(this.board);
        //FIXME: Qui ci sarà come il metodo del singleplayer che faceva fare la mossa....
    }
    private void passivePlay() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int choice = 0;
        System.out.println("It's not your turn now :(\nHowever you can still do all of these actions...");
        do {
            choice = uView.askAction();
            switch (choice) {
                case 1 -> {
                    System.out.println("Here's the game board...");
                    uView.showTUIBoard(board);
                }
                case 2 -> {
                    System.out.println("Here are the CommonGoalCards...");
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    uView.showPGC(listOfPlayers.get(myIndex));
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    for (Player player : listOfPlayers) {
                        if (listOfPlayers.indexOf(player) == myIndex)
                            System.out.println("This is yours....");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {break;}
                default -> System.err.println("Invalid value...");
            }
            if(choice!=6)
                System.out.println("What else would you like to do?");
        }while (choice!=6);

    }
}