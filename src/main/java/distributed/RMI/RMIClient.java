package distributed.RMI;

import distributed.Client;
import model.*;
import util.Cord;
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
        String nickname = uView.askPlayerNickname();
        errorReceived =server.sendMessage(nickname,SET_NICKNAME); //invia al server il nickname
        while (errorReceived!=Error.OK) {
            System.out.println("Server sent this error: " + errorReceived);
            switch (errorReceived) {
                case EMPTY_NICKNAME -> System.err.println("Nickname is empty...");
                case NOT_AVAILABLE -> System.err.println("Nickname already taken...");
            }
            errorReceived = server.sendMessage(uView.askPlayerNickname(), SET_NICKNAME);
        }
        myIndex = (int) server.getModel(SET_INDEX,nickname);
        if(myIndex==0){
            errorReceived = server.sendMessage(null,ALL_CONNECTED);
            while (errorReceived != Error.OK) {
                errorReceived = server.sendMessage(null, ALL_CONNECTED);
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
    private boolean hasGameStarted = false;
    public void getModel() throws IOException {
        Error errorReceived = Error.WAIT;
        if(!hasGameStarted) {
            errorReceived = server.sendMessage(null, GAME_STARTED);
            while (errorReceived != Error.OK)
                errorReceived = server.sendMessage(null, GAME_STARTED);
        }
        this.board =(Board) server.getModel(GAME_BOARD,myIndex);
        this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS,myIndex);
        this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(GAME_CGC,myIndex);
        this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(GAME_PGC, myIndex);
        this.indexOfPIT = (int) server.getModel(GAME_PIT,myIndex); //This variable/attribute can be used to check if this client is the player in turn (if so he has to make moves on the board)
        this.playerInTurn = listOfPlayers.get(indexOfPIT);
        System.out.println("It's "+playerInTurn.getNickname()+"'s turn!");
        System.out.println(indexOfPIT+" "+myIndex);
        if(myIndex==indexOfPIT) {
            activePlay();
        }
        else {
            passivePlay();
            getModel();
        }
    }
    private void activePlay() throws IOException {
        Error errorReceived;
        playerInTurn = listOfPlayers.get(myIndex);
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
        //Asking the player the amount of tiles they wish to pick and the coordinates
        do {
            numOfChosenTiles();
            errorReceived = server.sendMessage(numberOfChosenTiles, TURN_AMOUNT);
            switch (errorReceived) {
                case INVALID_VALUE -> { System.err.println("This number is too big!\nThere is not enough space in your board for these many tiles...."); }
                case OUT_OF_BOUNDS -> { System.err.println("Invalid Value...");}
            }
        }while (errorReceived != Error.OK);
        //Asks the client the coordinates
        do {
            chooseTiles();
            System.out.println(cords.get(0).getRowCord()+" "+cords.get(0).getColCord());
            errorReceived = server.sendMessage(cords, TURN_PICKED_TILES);
            System.out.println(errorReceived);
            System.out.flush();
            switch (errorReceived) {
                case BLOCKED_NOTHING -> {System.err.println("You are trying to pick up a tile that doesn't exist..."); uView.showTUIBoard(this.board);}
                case NOT_ON_BORDER -> { System.err.println("This tile cannot be picked up right now..."); uView.showTUIBoard(this.board);}
                case NOT_ADJACENT -> {System.err.println("You can only pick up tiles that are adjacent one to the other..."); uView.showTUIBoard(this.board);}
            }
        }while (errorReceived !=Error.OK);
        //Asking in which column the player wishes to place the picked tiles
        ArrayList<Tile> tilesInHand = new ArrayList<>();
        tilesInHand = (ArrayList<Tile>) server.getModel(TURN_TILE_IN_HAND,myIndex);
        uView.printTilesInHand(tilesInHand);
        uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
        int column;
        do {
            column = uView.askColumn();
            errorReceived = server.sendMessage(column,TURN_COLUMN);
            switch (errorReceived) {
                case INVALID_VALUE -> System.err.println("Invalid value...");
                case OUT_OF_BOUNDS -> System.err.println("There is not enough space to place all the tiles you picked in this column...");
            }
        }while (errorReceived!=Error.OK);
        //Asking the order of insertion
        int pos;
        for(int i = 0; i<numberOfChosenTiles; i++) {
            do {
                pos = uView.askTileToInsert(tilesInHand);
                errorReceived = server.sendMessage(pos, TURN_POSITION);
            }while (errorReceived!=Error.OK);
            playerInTurn.getMyBookshelf().placeTile(column,tilesInHand.get(pos));
            tilesInHand =(ArrayList<Tile>) server.getModel(TURN_POSITION,myIndex);
            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
        errorReceived = server.sendMessage(playerInTurn.getMyBookshelf(),UPDATE_BOOKSHELF);
        errorReceived = server.sendMessage(null,CHECK_REFILL);
        if(errorReceived==Error.REFILL)
            System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
        System.out.println("Would you like to do anything else before completing your turn?\n1)Yes\n2)No");
        choice = Integer.parseInt(reader.readLine());
        if(choice==1)
            passivePlay(); //FIXME passivePlay looperà finchè non sarà il turno di quel client quindi chiamarlo quì non sarà possibile
        errorReceived = server.sendMessage(myIndex,END_OF_TURN);
        getModel();
    }
    private void passivePlay() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int choice = 0;
        Error status = Error.WAIT;
        do {
            choice = uView.askAction();
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
                case 6 -> {
                    status = server.sendMessage(myIndex,CHECK_MY_TURN);
                }
                default -> System.err.println("Invalid value...");
            }
            if(choice!=6)
                System.out.println("What else would you like to do?");
        }while (status!=Error.OK);
    }

    private ArrayList<Cord> cords = new ArrayList<>();
    private int numberOfChosenTiles;
    public void chooseTiles() throws IOException {
        cords.removeAll(cords);
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
    }

    private boolean isTileFreeTile(Cord cord) {
        int x = cord.getRowCord();
        int y = cord.getColCord();
        boolean valid = false;
        if(board.getSelectedType(x + 1, y) == Type.NOTHING || board.getSelectedType(x, y + 1) == Type.NOTHING || board.getSelectedType(x - 1, y) == Type.NOTHING || board.getSelectedType(x, y - 1) == Type.NOTHING)
            valid=true;
        if(board.getSelectedType(x + 1, y) == Type.BLOCKED || board.getSelectedType(x, y + 1) == Type.BLOCKED || board.getSelectedType(x - 1, y) == Type.BLOCKED || board.getSelectedType(x, y - 1) == Type.BLOCKED)
            valid=true;
        return valid;
    }

    private void numOfChosenTiles() throws IOException {
        this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
    }
    /*
    TXT PER ME:
    Il client inserisce il numero di Tiles che vuole estrarre (Lo controlla il controller del server o del client? -> dato che il Giampi aveva detto che vanno avvertiti tutti i clients (Non so come si farà))
    per ora supponiamo che il controllo venga fatto sul client.
    Inserisce la prima coordinata e la manda al server.
        Il server ora controlla se questa coordinata:
            1 Ha senso
            2 Non è blocked/Nothing
            3 E' Estraibile
            4 Se non è la prima che gli viene passata controlla che sia vicino alle altre mandate in precedenza
        Restituendo di conseguenza il corrispondente errore.
    Una volta che il server completa questi controlli sarà lui stesso a rimuovere le Tiles dalla board
    e una volta rimosse farà la notify a tutti i clients (incluso quello che ha fatto la mossa)
    FIXME:Come fa così il client ad avere le TilesInHand?
    il client quindi mostra all'utente le tilesInHand e chiede al client la posizione e l'ordine con cui le vuole iniserire nella Bookshelf
    (Nello stesso modo di prima? Cioè che il client decide quale tile e quale colonna ed è il server a fare queste cose o è lui che lo fa e il server solo alla fine controlla che non ci sono stati problemi???)
     */

}