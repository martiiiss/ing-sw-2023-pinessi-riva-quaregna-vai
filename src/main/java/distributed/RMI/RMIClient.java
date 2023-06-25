package distributed.RMI;


import distributed.messages.Message;
import model.*;
import util.Cord;
import util.Event;
import view.GUI.GUIView;
import view.UserView;
import java.io.*;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import static java.lang.System.*;
import static util.Event.*;

/**Class that represents an RMI Client*/
public class RMIClient extends UnicastRemoteObject implements Serializable,ClientInterface{
    @Serial
    private static final long serialVersionUID = -3489512533622391685L;
    private transient ServerRMIInterface server;
    private final String address;
    private int port;
    private Event errorReceived;
    private int myIndex;
    private Object lock;
    private int viewChosen;
    private  GUIView gui;
    private UserView uView;
    private boolean isFirstTurn = true;
    private int matchIndex;
    private int numOfPlayers = 0;
    private Thread threadWaitTurn;
    private Thread threadEndGame;
    private Board board;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private ArrayList<Player> listOfPlayers;
    private Player playerInTurn;
    private int indexOfPIT;
    private boolean hasGameStarted = false;
    private ArrayList<Cord> cords = new ArrayList<>();
    private int numberOfChosenTiles;
    private int column;
    private ArrayList<Cord> tilesCords = new ArrayList<>();
    private boolean disabledInput;

    public RMIClient(String address, int port) throws RemoteException {
        this.address = address;
        this.port = port;
    }


    public void startConnection() throws RemoteException {
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            ArrayList<Integer> indexFromServer;
            indexFromServer = server.initClient(this);
            this.myIndex = indexFromServer.get(1);
            this.matchIndex = indexFromServer.get(0);
            out.println("GameIndex: "+matchIndex+" PlayerIndex"+myIndex);
            Thread connection = new Thread(controlDisconnection(),"ControlDisconnection");
            connection.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int askNumOfPlayers() throws IOException {
        UserView userView = new UserView();
        numOfPlayers = userView.askNumOfPlayer();
        while (numOfPlayers < 2 || numOfPlayers > 4) {
            System.err.println("Retry...");
            numOfPlayers = userView.askNumOfPlayer();
        }
        return numOfPlayers;
    }


    public void lobby() throws IOException, InterruptedException {
        this.lock = new Object();
        this.uView = new UserView();
        this.viewChosen = uView.userInterface();
        errorReceived = server.sendMessage(this.matchIndex, this.viewChosen,CHOOSE_VIEW); //invia al server la View desiderato
        while (errorReceived != GUI_VIEW && errorReceived != TUI_VIEW) {
            System.out.println(errorReceived.getMsg());
            this.viewChosen = uView.userInterface();
            errorReceived = server.sendMessage(this.matchIndex,this.viewChosen,CHOOSE_VIEW);
        }
        String nickname = uView.askPlayerNickname();
        errorReceived = server.sendMessage(this.matchIndex,nickname, SET_NICKNAME); //invia al server il nickname
        while (errorReceived != Event.OK) {
            System.out.println(errorReceived.getMsg());
            errorReceived = server.sendMessage(this.matchIndex,uView.askPlayerNickname(), SET_NICKNAME);
        }
        myIndex = (int) server.getModel(this.matchIndex,SET_INDEX, nickname);
        if (myIndex == 0) {
            errorReceived = server.sendMessage(this.matchIndex,null, ALL_CONNECTED);
            while (errorReceived != Event.OK) {
                errorReceived = server.sendMessage(this.matchIndex,null, ALL_CONNECTED);
            }
        }
        this.threadEndGame = new Thread(()-> {
            try {
                checkEndGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.threadWaitTurn = new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        waitTurn();
                        lock.wait();
                    } catch (InterruptedException | IOException e) {
                        //throw new RuntimeException(e);
                    }
                }

            }
        });
        getModel();
    }


    public void getModel() throws IOException, InterruptedException {
        out.println("In model");
        disabledInput = false;
        errorReceived = Event.WAIT;
        if (!hasGameStarted) {
            errorReceived = server.sendMessage(this.matchIndex,null, GAME_STARTED);
            while (errorReceived != Event.OK)
                errorReceived = server.sendMessage(this.matchIndex,null, GAME_STARTED);
        }
        this.board = (Board) server.getModel(this.matchIndex,GAME_BOARD, myIndex);
        this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex);
        this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(this.matchIndex,GAME_CGC, myIndex);
        this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(this.matchIndex,GAME_PGC, myIndex);
        this.indexOfPIT = (int) server.getModel(this.matchIndex,GAME_PIT, myIndex); //This variable/attribute can be used to check if this client is the player in turn (if so he has to make moves on the board)
        this.playerInTurn = listOfPlayers.get(indexOfPIT);
        if(!threadEndGame.isAlive())
           // threadEndGame.start();

        if (this.viewChosen == 1) {
            out.println("observers: " + board.getObservers());

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
            if (this.isFirstTurn) {
                gui = new GUIView();
               // this.board.addObserver(gui);
                server.sendMessage(matchIndex, gui, ADD_OBSERVER);
                this.isFirstTurn = false;
                gui.updateBoard(this.board);
                gui.setupCGC((CommonGoalCard) ((ArrayList<?>) server.getModel(this.matchIndex,GAME_CGC, myIndex)).get(0));
                gui.setupCGC((CommonGoalCard) ((ArrayList<?>) server.getModel(this.matchIndex,GAME_CGC, myIndex)).get(1));
                gui.setupPGC(((PersonalGoalCard) server.getModel(this.matchIndex,GAME_PGC, myIndex)).getNumber());
            }
            if (myIndex == indexOfPIT) {
                flowGui();
                getModel();
            } else {
                synchronized (lock) {
                    this.lock.notifyAll();
                }
                if(!threadWaitTurn.isAlive()){
                    threadWaitTurn.start();
                }
                Event status = Event.WAIT;
                do {
                    try {
                        status = server.sendMessage(this.matchIndex,myIndex, CHECK_MY_TURN);
                        Event e = server.sendMessage(this.matchIndex, myIndex, SET_UP_BOARD);
                        if(e==SET_UP_BOARD) {
                            board = (Board) server.getModel(matchIndex, GAME_BOARD, myIndex);
                            gui.update(board, new Message(board, SET_UP_BOARD));
                        }
                    } catch (SocketException | UnmarshalException ex) {}
                } while (status != Event.OK);
                getModel();
            }
        }
    }

    private void activePlay() throws IOException, InterruptedException {
        isFirstTurn = false;
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
        ArrayList<Tile> tilesInHand;
        tilesInHand = (ArrayList<Tile>) server.getModel(this.matchIndex,TURN_TILE_IN_HAND, myIndex);
        uView.printTilesInHand(tilesInHand);
        uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
        //Asking in which column the player wishes to place the picked tiles
        do {
            errorReceived = activeAskColumn(tilesInHand);
        } while (errorReceived != Event.OK);
        //Asking the order of insertion
        errorReceived = server.sendMessage(this.matchIndex,playerInTurn.getMyBookshelf(), UPDATE_BOOKSHELF);
        errorReceived = server.sendMessage(this.matchIndex,null, CHECK_REFILL);

        //The server checks if the board had to be refilled, the client asks the server
        //if it has been done, if true then it receives an update of the board so that it can be printed
        if (errorReceived == Event.REFILL)
            System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
        errorReceived = server.sendMessage(this.matchIndex,myIndex, END_OF_TURN);
        System.out.println(errorReceived.getMsg());
        /*if (errorReceived == GAME_OVER) {
            uView.gameOver(listOfPlayers);
            System.exit(0);
        }*/
        getModel();
    }

    private void passivePlay() throws IOException, InterruptedException {
        System.out.println("It's not your turn, here are some actions you can do!");
        Event status;
        do {
            status = server.sendMessage(this.matchIndex,myIndex, CHECK_MY_TURN);
            if (status != Event.OK)
                passivePlayerMenu();
        } while (status != Event.OK);
        getModel();
    }


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
    }

    private void numOfChosenTiles() {
        this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
    }

    private void activePlayerMenu() throws IOException {
        int choice;
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
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 7 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex);
                    uView.showPlayers(listOfPlayers);
                }
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
    }

    private Event activeAskNumOfTiles() throws IOException {
        numOfChosenTiles();
        errorReceived = server.sendMessage(this.matchIndex,numberOfChosenTiles, TURN_AMOUNT);
        System.out.println(errorReceived.getMsg());
        return errorReceived;
    }

    private Event activeAskTiles() throws IOException {
        chooseTiles();
        errorReceived = server.sendMessage(this.matchIndex,cords, TURN_PICKED_TILES);
        System.out.println(errorReceived.getMsg());
        return errorReceived;
    }

    private Event activeAskColumn(ArrayList<Tile> tilesInHand) throws IOException {
        column = uView.askColumn();
        errorReceived = server.sendMessage(this.matchIndex,column, TURN_COLUMN);
        System.out.println(errorReceived.getMsg());
        if (errorReceived == Event.OK)
            activePlaceTile(tilesInHand);
        return errorReceived;
    }

    private void activePlaceTile(ArrayList<Tile> tilesInHand) throws IOException {
        int pos;
        for (int i = 0; i < numberOfChosenTiles; i++) {
            do {
                pos = uView.askTileToInsert(tilesInHand);
                errorReceived = server.sendMessage(this.matchIndex,pos, TURN_POSITION);
                System.out.println(errorReceived.getMsg());
            } while (errorReceived != Event.OK);
            playerInTurn.setMyBookshelf((Bookshelf) server.getModel(this.matchIndex,UPDATE_BOOKSHELF, myIndex));
            tilesInHand = (ArrayList<Tile>) server.getModel(this.matchIndex,TURN_POSITION, myIndex);
            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
    }

    private void passivePlayerMenu() throws IOException {
        uView.askPassiveAction();
        int choice = uView.waitInput();
        if (!disabledInput) {
            switch (choice) {
                case 1 -> {
                    this.board = (Board) server.getModel(this.matchIndex,GAME_BOARD, myIndex);
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
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex); //get the latest update
                    uView.showPlayers(listOfPlayers);
                }
            }
        }
    }


    public void flowGui() throws IOException{
        int tilesToPick;
        do {
            tilesToPick = gui.askTiles(); //ask number of tiles
            errorReceived = server.sendMessage(this.matchIndex,tilesToPick, TURN_AMOUNT);
            gui.showError(errorReceived);
        } while (errorReceived != Event.OK);

        do {
            gui.getBoardView().setTilesPicked(tilesToPick);
            System.out.println("tiles picked " + gui.getBoardView().getTilesPicked());
            tilesCords = gui.getTilesClient();
            System.out.println("tiles cords " + tilesCords.size());
            errorReceived = server.sendMessage(this.matchIndex,tilesCords, TURN_PICKED_TILES);
            System.out.println(errorReceived.getMsg());
            gui.showError(errorReceived);
            System.out.println("errore: " + errorReceived.getMsg());
        } while (errorReceived != Event.OK);

        ArrayList<Tile> tilesInHand = (ArrayList<Tile>) server.getModel(this.matchIndex,TURN_TILE_IN_HAND, myIndex);

        gui.pickTiles(tilesCords, tilesInHand); //aggiunge le tessere alla "mano"

        do {
            int column = gui.chooseColumn();
            errorReceived = server.sendMessage(this.matchIndex, column, TURN_COLUMN);
            System.out.println("errore colonna: " + errorReceived);
            gui.showError(errorReceived);
        }while(errorReceived!=Event.OK);

        for(int i=0; i<tilesToPick; i++){
            int pos = gui.chooseTile(); //sceglie quale tessera mettere in una colonna: restituisce la posizione nella mano
            gui.addTile(tilesInHand.get(pos));
            errorReceived = server.sendMessage(this.matchIndex, pos, TURN_POSITION);
        }
        gui.getHandView().setTileToInsert(-1);

        //FIXME sistemare i seguenti (fine)
        errorReceived = server.sendMessage(this.matchIndex,null, CHECK_REFILL);
        if (errorReceived == Event.REFILL) {
            //visualizzare errore
            gui.showError(errorReceived);
        }
        errorReceived = server.sendMessage(this.matchIndex,myIndex, END_OF_TURN);
        if (errorReceived == GAME_OVER) {
            gui.results(listOfPlayers.get(myIndex).getNickname(),listOfPlayers.get(myIndex).getScore());
            gui.showError(errorReceived);
            //System.exit(0);
        }
    }

    public void waitTurn() throws IOException, InterruptedException {
        int pitIndex;
        do {
            pitIndex = (int) server.getModel(this.matchIndex,GAME_PIT, myIndex);
        } while (pitIndex != myIndex);
        if(viewChosen==1) {
            System.out.println("You turn");
            System.out.println("Press enter to start your turn....");
        }
        disabledInput = true;
    }
    public Runnable controlDisconnection() {
        Thread controlDisconnectionThread = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if(server.getDisconnection(matchIndex)) {
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
    private Runnable checkEndGame() throws IOException {//TODO change name to thread
        Event errRec;
        do {
            errRec = server.sendMessage(matchIndex, null, CHECK_ENDGAME);
            this.board = (Board) server.getModel(matchIndex,GAME_BOARD,myIndex);
            gui.update(board,new Message(board,SET_UP_BOARD));
            this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(matchIndex,GAME_CGC,myIndex);
            for (CommonGoalCard cgc : commonGoalCard)
                gui.update(cgc, new Message(commonGoalCard,UPDATE_SCORINGTOKEN));

        }while (errRec!=GAME_OVER);
        uView.gameOver(listOfPlayers);
        threadWaitTurn.interrupt();
        Thread.currentThread().interrupt();
        System.exit(0);
        return null;
    }
}
