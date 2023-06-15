package distributed.RMI;

import distributed.Client;
import model.*;
import util.Cord;
import util.Event;
import view.UserView;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Stack;

import static java.lang.System.*;
import static util.Event.*;

public class RMIClient extends Client implements Serializable {
    @Serial
    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private int port;
    private UserView uView = new UserView();
    private Event errorReceived;
    private int myIndex;

    private Object lock;

    public RMIClient(String address, int port) throws RemoteException {
        super(address, port);
        this.address = address; //server
        this.port = port;
    }

    //FIXME: ci sraà da cambiare la exit con il mutlipartita. Cioè se il client dovesse provare a connettersi ad una partita già esistente ma full non deve terminare il processo ma deve avere la possibilità
    @Override
    public void startConnection() throws RemoteException, NotBoundException {
        try {
            server = (ServerRMIInterface) Naming.lookup(getUsername());
            this.myIndex = server.initClient(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(this.myIndex==-1) {
            System.err.println("The server is full..");
            exit(myIndex);
        }
    }


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
    Thread threadWaitTurn;
    private boolean isFirstTurn = true;


    public void lobby() throws IOException { //TODO Metti il sincro per la stampa brutta
        this.lock = new Object();
        Event errorReceived;
        if(myIndex==0) {//Vuol dire che sono il primo client connesso
            numOfPlayers = uView.askNumOfPlayer();
            while (numOfPlayers<2 || numOfPlayers>4) {
                System.err.println("Retry...");
                numOfPlayers = uView.askNumOfPlayer();
            }
            errorReceived = server.sendMessage(numOfPlayers, ASK_NUM_PLAYERS); //invia al server il numero di giocatori
            System.out.println(errorReceived.getMsg());
        }
        errorReceived = server.sendMessage(uView.userInterface(),CHOOSE_VIEW); //invia al server la View desiderata
        while (errorReceived!=Event.OK) {
            System.out.println(errorReceived.getMsg());
            errorReceived = server.sendMessage(uView.userInterface(),CHOOSE_VIEW);
        }
        String nickname = uView.askPlayerNickname();
        errorReceived =server.sendMessage(nickname,SET_NICKNAME); //invia al server il nickname
        while (errorReceived!=Event.OK) {
            System.out.println(errorReceived.getMsg());
            errorReceived = server.sendMessage(uView.askPlayerNickname(), SET_NICKNAME);
        }
        myIndex = (int) server.getModel(SET_INDEX,nickname);
        if(myIndex==0){
            errorReceived = server.sendMessage(null,ALL_CONNECTED);
            while (errorReceived != Event.OK) {
                errorReceived = server.sendMessage(null, ALL_CONNECTED);
            }
        }
        System.out.println("Pre thread");
        this.threadWaitTurn = new Thread(()->{
            System.out.println("lock");
            synchronized (lock){
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        System.out.println("entra: ");
                        waitTurn();
                        lock.wait();
                        System.out.println("esce ");
                    } catch (InterruptedException | RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        threadWaitTurn.start();
        System.out.println("After thread");
        getModel();
    }

    private Board board;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private ArrayList<Player> listOfPlayers;
    private Stack<ScoringToken> tokenStack;
    private Player playerInTurn;
    private int indexOfPIT;
    private boolean hasGameStarted = false;
    public void getModel() throws IOException {
        disabledInput = false;
        Event errorReceived = Event.WAIT;
        if(!hasGameStarted) {
            errorReceived = server.sendMessage(null, GAME_STARTED);
            while (errorReceived != Event.OK)
                errorReceived = server.sendMessage(null, GAME_STARTED);
        }
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
        else {
            synchronized (lock) {
                System.out.println("Risveglio il lock");
                this.lock.notifyAll();
                }
            passivePlay();
            getModel();
        }
    }
    private void activePlay() throws IOException {

        Event errorReceived;
        playerInTurn = listOfPlayers.get(myIndex);
        //SHOWS THE PLAYER ALL THE ACTIONS THEY CAN DO
        activePlayerMenu();
        System.out.println("Here's your Bookshelf:");
        uView.showTUIBookshelf(listOfPlayers.get(indexOfPIT).getMyBookshelf());
        uView.showTUIBoard(this.board);
        //Asking the player the amount of tiles they wish to pick and the coordinates
        do {
            errorReceived = activeAskNumOfTiles();
        }while (errorReceived != Event.OK);
        //Asks the client the coordinates
        do {
            errorReceived = activeAskTiles();
        }while (errorReceived !=Event.OK);
        ArrayList<Tile> tilesInHand = new ArrayList<>();
        tilesInHand = (ArrayList<Tile>) server.getModel(TURN_TILE_IN_HAND,myIndex);
        uView.printTilesInHand(tilesInHand);
        uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
        //Asking in which column the player wishes to place the picked tiles
        do {
           errorReceived = activeAskColumn(tilesInHand);
        }while (errorReceived!=Event.OK);
        //Asking the order of insertion
        errorReceived = server.sendMessage(playerInTurn.getMyBookshelf(),UPDATE_BOOKSHELF); //FIXME non sono sicuro che si possa fare così, credo sia il controller che deve aggiornare la bookshelf di player
        errorReceived = server.sendMessage(null,CHECK_REFILL);

        //The server checks if the board had to be refilled, the client asks the server
        //if it has been done, if true then it receives an update of the board so that it can be printed
        if(errorReceived==Event.REFILL)
            System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
        errorReceived = server.sendMessage(myIndex,END_OF_TURN);
        System.out.println(errorReceived.getMsg());
        if(errorReceived == GAME_OVER) {
            System.out.println("FANGULO STOGGIIOCOD IMMMERDA");
            System.exit(0);
        }
        getModel();
    }
    private void passivePlay() throws IOException {
        System.out.println("It's not your turn, here are some actions you can do!");
        Event status = Event.WAIT;
        do {
            status = server.sendMessage(myIndex,CHECK_MY_TURN);
            if(status!=Event.OK)
                passivePlayerMenu(status);
        }while (status!=Event.OK);
        getModel();
    }

    private ArrayList<Cord> cords = new ArrayList<>();
    private int numberOfChosenTiles;
    public void chooseTiles() throws IOException {
        cords.removeAll(cords);
        System.out.println("Size prima della richiesta "+cords.size());
        while (cords.size()<this.numberOfChosenTiles) {
            Cord cord = new Cord();
            do {
                String in = uView.askTilePosition();
                try {
                    String[] splittedStr = in.split(",");
                    cord.setCords(Integer.parseInt(splittedStr[0]), Integer.parseInt(splittedStr[1]));
                } catch (NumberFormatException formatException) {
                    System.err.println("Invalid format...");
                } catch (ArrayIndexOutOfBoundsException boundsException) {
                    System.err.println("Invalid format or non existent coordinate...");
                }
            } while (cord.getRowCord() == 0 && cord.getColCord() == 0);
            cords.add(cord);
        }
        System.out.println("Size dopo della richiesta "+cords.size());
    }

    private void numOfChosenTiles() throws IOException {
        this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
    }

    private void activePlayerMenu() throws IOException {
        int choice = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        System.out.println("Would you like to do any of these actions before making your move?");
        do {
            //TODO:Sposta l'opzione "continua con il turno" ad un numero più decente di "6"
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
                    uView.showPGC(listOfPlayers.get(myIndex).getPersonalGoalCard());
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m"+player.getNickname()+"\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 7 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex);
                    uView.showPlayers(listOfPlayers);
                }
            }
            if(choice!=6)
                System.out.println("What else would you like to do?");
        }while (choice!=6);
    }
    private Event activeAskNumOfTiles() throws IOException {
        numOfChosenTiles();
        errorReceived = server.sendMessage(numberOfChosenTiles, TURN_AMOUNT);
        System.out.println(errorReceived.getMsg());
        return errorReceived;
    }

    private Event activeAskTiles() throws IOException {
        chooseTiles();
        errorReceived = server.sendMessage(cords, TURN_PICKED_TILES);
        System.out.println(errorReceived.getMsg());
        return errorReceived;
    }
    int column;
    private Event activeAskColumn(ArrayList<Tile> tilesInHand) throws IOException {
        column = uView.askColumn();
        errorReceived = server.sendMessage(column,TURN_COLUMN);
        System.out.println(errorReceived.getMsg());
        if(errorReceived == Event.OK)
            activePlaceTile(column,tilesInHand);
        return errorReceived;
    }
    private void activePlaceTile(int column, ArrayList<Tile> tilesInHand) throws IOException {
        int pos;
        for(int i = 0; i<numberOfChosenTiles; i++) {
            do {
                pos = uView.askTileToInsert(tilesInHand);
                errorReceived = server.sendMessage(pos, TURN_POSITION);
                System.out.println(errorReceived.getMsg());
            }while (errorReceived!=Event.OK);
            playerInTurn.setMyBookshelf((Bookshelf) server.getModel(UPDATE_BOOKSHELF,myIndex));
            tilesInHand =(ArrayList<Tile>) server.getModel(TURN_POSITION,myIndex);
            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
    }
    private void passivePlayerMenu(Event status) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        uView.askPassiveAction();
        int choice = uView.waitInput();
        if(!disabledInput){
            switch (choice) {
                case 1 -> {
                    this.board =(Board) server.getModel(GAME_BOARD,myIndex);
                    System.out.println("Here's the game board...");
                    uView.showTUIBoard(board);
                }
                case 2 -> {
                    System.out.println("Here are the CommonGoalCards...");
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    uView.showPGC(listOfPlayers.get(myIndex).getPersonalGoalCard());
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m"+player.getNickname()+"\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex); //get the latest update
                    uView.showPlayers(listOfPlayers);
                }
            }
        }

    }
    private boolean disabledInput;

    public void waitTurn() throws RemoteException, InterruptedException {
        int pitIndex;
        do {
            //System.out.println("true");
            pitIndex = (int) server.getModel(GAME_PIT, myIndex);
            //System.out.println("jk " + pitIndex);
        }while (pitIndex != myIndex);
        System.out.println("You turn");
        System.out.println("Press enter to start your turn....");
        disabledInput = true;

        //this.lock.wait();
        //this.threadWaitTurn.wait();

        }

//FIXME:Al momento il metodo del calcolo punteggio è sbagliato, non tiene conto dei punteggi aggiunti in precedenza
}
