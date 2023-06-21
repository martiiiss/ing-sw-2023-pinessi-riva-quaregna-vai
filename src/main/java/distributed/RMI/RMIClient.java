package distributed.RMI;

import distributed.Client;
import model.*;
import util.Cord;
import util.Event;
import view.GUI.GUIView;
import view.UserView;
import view.ViewInterface;

import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Stack;

import static java.lang.System.*;
import static util.Event.*;

public class RMIClient extends UnicastRemoteObject implements Serializable,ClientInterface{
    @Serial
    private static final long serialVersionUID = -3489512533622391685L; //random number
    private transient ServerRMIInterface server;
    private String address;
    private int port;
    private Event errorReceived;
    private int myIndex;

    private Object lock;
    private int viewChosen;
    private GUIView gui;
    private UserView uView;
    private boolean isFirstTurn = true;

    public RMIClient(String address, int port) throws RemoteException {
        this.address = address; //server
        this.port = port;
    }

    //FIXME: ci sraà da cambiare la exit con il mutlipartita. Cioè se il client dovesse provare a connettersi ad una partita già esistente ma full non deve terminare il processo ma deve avere la possibilità
    public void startConnection() throws RemoteException, NotBoundException {
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            Thread connection = new Thread(controlDisconnection(),"ControlDisconnection");
            connection.start();
            this.myIndex = server.initClient((ClientInterface) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.myIndex == -1) {
            System.err.println("The server is full..");
            exit(myIndex);
        }

    }

    int numOfPlayers = 0;
    Thread threadWaitTurn;


    public void lobby() throws IOException { //TODO Metti il sincro per la stampa brutta
        this.lock = new Object();
        Event errorReceived;
        this.uView = new UserView();
        if(myIndex==0) {//Vuol dire che sono il primo client connesso
            numOfPlayers = uView.askNumOfPlayer();
            while (numOfPlayers < 2 || numOfPlayers > 4) {
                System.err.println("Retry...");
                numOfPlayers = uView.askNumOfPlayer();
            }
            errorReceived = server.sendMessage(numOfPlayers, ASK_NUM_PLAYERS); //invia al server il numero di giocatori
            System.out.println(errorReceived.getMsg());
        }
        this.viewChosen = uView.userInterface();
        errorReceived = server.sendMessage(this.viewChosen,CHOOSE_VIEW); //invia al server la View desiderato
        while (errorReceived != GUI_VIEW && errorReceived != TUI_VIEW) {
            System.out.println(errorReceived.getMsg());
            this.viewChosen = uView.userInterface();
            errorReceived = server.sendMessage(this.viewChosen,CHOOSE_VIEW);
        }
        String nickname = uView.askPlayerNickname();
        errorReceived = server.sendMessage(nickname, SET_NICKNAME); //invia al server il nickname
        while (errorReceived != Event.OK) {
            System.out.println(errorReceived.getMsg());
            errorReceived = server.sendMessage(uView.askPlayerNickname(), SET_NICKNAME);
        }
        myIndex = (int) server.getModel(SET_INDEX, nickname);
        if (myIndex == 0) {
            errorReceived = server.sendMessage(null, ALL_CONNECTED);
            while (errorReceived != Event.OK) {
                errorReceived = server.sendMessage(null, ALL_CONNECTED);
            }
        }
        this.threadWaitTurn = new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        waitTurn();
                        lock.wait();
                    } catch (InterruptedException | RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
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
        out.println("In model");
        disabledInput = false;
        Event errorReceived = Event.WAIT;
        if (!hasGameStarted) {
            errorReceived = server.sendMessage(null, GAME_STARTED);
            while (errorReceived != Event.OK)
                errorReceived = server.sendMessage(null, GAME_STARTED);
        }
        this.board = (Board) server.getModel(GAME_BOARD, myIndex);
        this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS, myIndex);
        this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(GAME_CGC, myIndex);
        this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(GAME_PGC, myIndex);
        this.indexOfPIT = (int) server.getModel(GAME_PIT, myIndex); //This variable/attribute can be used to check if this client is the player in turn (if so he has to make moves on the board)
        this.playerInTurn = listOfPlayers.get(indexOfPIT);
        System.out.println("It's " + playerInTurn.getNickname() + "'s turn!");

        if (this.viewChosen == 1) {
            if (myIndex == indexOfPIT) {
                activePlay();
            } else {
                synchronized (lock) {
                    this.lock.notifyAll();
                }
                if(!threadWaitTurn.isAlive()){
                    threadWaitTurn.start();
                }
                passivePlay();
                getModel();
            }
        } else if (this.viewChosen == 2) {
            //setup iniziale: istanziare (una sola volta!!)
            if (this.isFirstTurn) {
                Bag bag = new Bag();
                board = new Board(4);
                Game game = new Game();
                game.setNumOfPlayers(4);
                game.setCommonGoalCards();
                ArrayList<Tile> tiles = bag.getBagTiles(board.getNumOfCells());
                gui = new GUIView();
                this.isFirstTurn = false;

                gui.updateBoard(board);
                gui.setupCGC((CommonGoalCard) ((ArrayList) server.getModel(GAME_CGC, myIndex)).get(0));
                gui.setupCGC((CommonGoalCard) ((ArrayList) server.getModel(GAME_CGC, myIndex)).get(1));
                gui.setupPGC((int) ((PersonalGoalCard) server.getModel(GAME_PGC, myIndex)).getNumber());

            }
            if (myIndex == indexOfPIT) {
                flowGui();
            }
        }
    }

    private void activePlay() throws IOException {
        isFirstTurn = false;
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
        } while (errorReceived != Event.OK);
        //Asks the client the coordinates
        do {
            errorReceived = activeAskTiles();
        } while (errorReceived != Event.OK);
        ArrayList<Tile> tilesInHand = new ArrayList<>();
        tilesInHand = (ArrayList<Tile>) server.getModel(TURN_TILE_IN_HAND, myIndex);
        uView.printTilesInHand(tilesInHand);
        uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
        //Asking in which column the player wishes to place the picked tiles
        do {
            errorReceived = activeAskColumn(tilesInHand);
        } while (errorReceived != Event.OK);
        //Asking the order of insertion
        errorReceived = server.sendMessage(playerInTurn.getMyBookshelf(), UPDATE_BOOKSHELF); //FIXME non sono sicuro che si possa fare così, credo sia il controller che deve aggiornare la bookshelf di player
        errorReceived = server.sendMessage(null, CHECK_REFILL);

        //The server checks if the board had to be refilled, the client asks the server
        //if it has been done, if true then it receives an update of the board so that it can be printed
        if (errorReceived == Event.REFILL)
            System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
        errorReceived = server.sendMessage(myIndex, END_OF_TURN);
        System.out.println(errorReceived.getMsg());
        if (errorReceived == GAME_OVER) {
            System.out.println("FANGULO STOGGIIOCOD IMMMERDA");
            System.exit(0);
        }
        getModel();
    }

    private void passivePlay() throws IOException {
        System.out.println("It's not your turn, here are some actions you can do!");
        Event status = Event.WAIT;
        do {
            status = server.sendMessage(myIndex, CHECK_MY_TURN);
            if (status != Event.OK)
                passivePlayerMenu(status);
        } while (status != Event.OK);
        getModel();
    }

    private ArrayList<Cord> cords = new ArrayList<>();
    private int numberOfChosenTiles;

    public void chooseTiles() throws IOException {
        cords.removeAll(cords);
        System.out.println("Size prima della richiesta " + cords.size());
        while (cords.size() < this.numberOfChosenTiles) {
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
        System.out.println("Size dopo della richiesta " + cords.size());
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
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS, myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 7 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS, myIndex);
                    uView.showPlayers(listOfPlayers);
                }
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
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
        errorReceived = server.sendMessage(column, TURN_COLUMN);
        System.out.println(errorReceived.getMsg());
        if (errorReceived == Event.OK)
            activePlaceTile(column, tilesInHand);
        return errorReceived;
    }

    private void activePlaceTile(int column, ArrayList<Tile> tilesInHand) throws IOException {
        int pos;
        for (int i = 0; i < numberOfChosenTiles; i++) {
            do {
                pos = uView.askTileToInsert(tilesInHand);
                errorReceived = server.sendMessage(pos, TURN_POSITION);
                System.out.println(errorReceived.getMsg());
            } while (errorReceived != Event.OK);
            playerInTurn.setMyBookshelf((Bookshelf) server.getModel(UPDATE_BOOKSHELF, myIndex));
            tilesInHand = (ArrayList<Tile>) server.getModel(TURN_POSITION, myIndex);
            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
    }

    private void passivePlayerMenu(Event status) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        uView.askPassiveAction();
        int choice = uView.waitInput();
        if (!disabledInput) {
            switch (choice) {
                case 1 -> {
                    this.board = (Board) server.getModel(GAME_BOARD, myIndex);
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
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS, myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(GAME_PLAYERS, myIndex); //get the latest update
                    uView.showPlayers(listOfPlayers);
                }
            }
        }
    }
//FIXME:Al momento il metodo del calcolo punteggio è sbagliato, non tiene conto dei punteggi aggiunti in precedenza




    public void flowGui() throws IOException {
        ArrayList<Cord> tilesCords;
        tilesCords = gui.askTiles();
        do{
            errorReceived = server.sendMessage(cords, TURN_PICKED_TILES);
            // NB: messaggio di errore lato gui se tiles non possono essere prese
        } while(errorReceived != Event.OK);

        ArrayList<Tile> tilesList = new ArrayList<>();
        for(int i=0; i<tilesCords.size(); i++){
            tilesList.add(board.getBoard()[tilesCords.get(i).getRowCord()][tilesCords.get(i).getColCord()]);
        }
        gui.pickTiles(tilesCords, tilesList); //aggiunge le tessere alla "mano"

        int column = gui.chooseColumn();
        //controllare colonna restituita
        do {
            errorReceived = server.sendMessage(column, TURN_COLUMN);
            //nb: messaggio d'errore se colonna sbagliata
        }while(errorReceived!=Event.OK);

        for(int i=0; i<numberOfChosenTiles; i++){ //TODO controllare dove viene settato numberOfChoosenTiles
            int pos = gui.chooseTile(); //sceglie quale tessera mettere in una colonna: restituisce la posizione nella mano
         //FIXME:
            // gui.addTile(pos); //aggiunge tessera a bookshelf
        }
    }

    private boolean disabledInput;

    public void waitTurn() throws RemoteException, InterruptedException {
        int pitIndex;
        do {
            //System.out.println("true");
            pitIndex = (int) server.getModel(GAME_PIT, myIndex);
            //System.out.println("jk " + pitIndex);
        } while (pitIndex != myIndex);
        System.out.println("You turn");
        System.out.println("Press enter to start your turn....");
        disabledInput = true;

        //this.lock.wait();
        //this.threadWaitTurn.wait();
    }
    public Runnable controlDisconnection() {
        Thread controlDisconnectionThread = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if(server.getDisconnection()) {
                        err.println("Somebody disconnected!");
                        Thread.currentThread().interrupt();
                        System.exit(-1);
                    }
                } catch (RemoteException e) {
                    System.err.println("Server crashed!");
                    Thread.currentThread().interrupt();
                    System.exit(-1);
                }
            }
        });
        controlDisconnectionThread.start();
        return null;
    }
    public void ping() throws RemoteException{
    }

}
