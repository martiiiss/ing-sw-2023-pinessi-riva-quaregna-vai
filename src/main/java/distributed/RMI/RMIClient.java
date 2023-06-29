package distributed.RMI;


import distributed.ClientInterface;
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
public class RMIClient extends UnicastRemoteObject implements Serializable, ClientInterface {
    @Serial
    private static final long serialVersionUID = -3489512533622391685L;
    private transient ServerRMIInterface server;
    private final String address;
    private Event errorReceived;
    private int myIndex;
    private Object lock;
    private int viewChosen;
    private  GUIView gui;
    private UserView uView;
    private boolean isFirstTurn = true;
    private int matchIndex;
    private Thread threadWaitTurn;
    private Board board;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private ArrayList<Player> listOfPlayers;
    private Player playerInTurn;
    private int indexOfPIT;
    private final ArrayList<Cord> cords = new ArrayList<>();
    private int numberOfChosenTiles;
    private boolean disabledInput;


    /**
     * Constructor of rhe Class, this initializes an RMI Client with the two given parameters.
     * @param port an int that represents the RMI port
     * @param address a {@code String} that represents the address of the server
     * @throws RemoteException if an error occurs during a remote call */
    public RMIClient(String address, int port) throws RemoteException {
        this.address = address;
    }


    /**
     *Method that starts the connection with the server.
     *
     *@throws RemoteException if an error occurs during a remote call*/
    public void startConnection() throws RemoteException {
        try {
            server = (ServerRMIInterface) Naming.lookup(address);
            ArrayList<Integer> indexFromServer;
            indexFromServer = server.initClient(this);
            this.myIndex = indexFromServer.get(1);
            this.matchIndex = indexFromServer.get(0);
            Thread connection = new Thread(controlDisconnection(),"ControlDisconnection");
            connection.start();
        } catch (Exception e) {
        }
    }


    /**
     * Method used to ask the first player of a match how many players it wants to play with.<br>
     * This method also performs a check on the input.
     * @return an int that represents the number of players chosen by the first player
     * @throws IOException if an error occurs*/
    public int askNumOfPlayers() throws IOException {
        UserView userView = new UserView();
        int numOfPlayers = userView.askNumOfPlayer();
        while (numOfPlayers < 2 || numOfPlayers > 4) {
            System.err.println("Retry...");
            numOfPlayers = userView.askNumOfPlayer();
        }
        return numOfPlayers;
    }


    /**
     * Method used to make the players wait while a match hasn't started yet.<br>
     * <p>
     *     In this method the player gets asked the type of user interface and the nickname;
     *     for every action the Clients sends a message to the Server and if the input given by the user was correct
     *     the user gets asked the next input, otherwise the client will receive an error and will be asked again.
     * </p>
     * This method also starts two treads: {@code threadEndGame} is used to check if the match is ending
     * and {@code threadWaitTurn} is used to handle the turn of a player that isn't in turn.
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
    public void lobby() throws IOException, InterruptedException {
        this.lock = new Object();
        this.uView = new UserView();
        this.viewChosen = uView.userInterface();
        errorReceived = server.sendMessage(this.matchIndex, this.viewChosen,CHOOSE_VIEW);
        while (errorReceived != GUI_VIEW && errorReceived != TUI_VIEW) {
            System.out.println(errorReceived.getTUIMsg());
            this.viewChosen = uView.userInterface();
            errorReceived = server.sendMessage(this.matchIndex,this.viewChosen,CHOOSE_VIEW);
        }
        String nickname = uView.askPlayerNickname();
        errorReceived = server.sendMessage(this.matchIndex,nickname, SET_NICKNAME);
        while (errorReceived != Event.OK) {
            System.out.println(errorReceived.getTUIMsg());
            errorReceived = server.sendMessage(this.matchIndex,uView.askPlayerNickname(), SET_NICKNAME);
        }
        if (myIndex == 0) {
            errorReceived = server.sendMessage(this.matchIndex,null, ALL_CONNECTED);
            while (errorReceived != Event.OK) {
                errorReceived = server.sendMessage(this.matchIndex,null, ALL_CONNECTED);
            }
        }

        this.threadWaitTurn = new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        waitTurn();
                        lock.wait();
                    } catch (InterruptedException | IOException e) {
                    }
                }
            }
        },"WaitForTurnThread");
        getModel();
    }


    /**
     * Method used to get updates in game from the model.<br>
     * This method manages to maintain coherence in real time of all the interfaces (TUI and GUI).
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
    public void getModel() throws IOException, InterruptedException {
        disabledInput = false;
        errorReceived = Event.WAIT;
        boolean hasGameStarted = false;
        if (!hasGameStarted) {

            errorReceived = server.sendMessage(this.matchIndex, null, GAME_STARTED);
            while (errorReceived != Event.OK)
                errorReceived = server.sendMessage(this.matchIndex, null, GAME_STARTED);
        }
        do {
            this.board = (Board) server.getModel(this.matchIndex, GAME_BOARD, myIndex);
            this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex, GAME_PLAYERS, myIndex);
            this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(this.matchIndex, GAME_CGC, myIndex);
            this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(this.matchIndex, GAME_PGC, myIndex);
            this.myPersonalGoalCard = (PersonalGoalCard) server.getModel(this.matchIndex, GAME_PGC, myIndex);
            do {
                this.indexOfPIT = (int) server.getModel(this.matchIndex, GAME_PIT, myIndex); //This variable/attribute can be used to check if this client is the player in turn (if so he has to make moves on the board)
            } while (indexOfPIT==-1);
            this.playerInTurn = listOfPlayers.get(indexOfPIT);
        }while (this.board==null || commonGoalCard == null || myPersonalGoalCard == null);
        if (this.viewChosen == 1) {
            if (myIndex == indexOfPIT) {
                out.println("my index " + myIndex + " pit " + indexOfPIT);
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
                gui.loadPlayers(listOfPlayers); //FIXME: Nulla da fixare solo il comando da copiare in Socket una volta finito
                server.sendMessage(matchIndex, gui, ADD_OBSERVER);
                this.isFirstTurn = false;
                while(board==null){
                    this.board = (Board) server.getModel(this.matchIndex,GAME_BOARD, myIndex);
                }
                gui.updateBoard(this.board);
                while(commonGoalCard==null || commonGoalCard.isEmpty()){
                    this.commonGoalCard = (ArrayList<CommonGoalCard>) server.getModel(this.matchIndex,GAME_CGC, myIndex);
                }
                gui.setupCGC((this.commonGoalCard).get(0));
                gui.setupCGC((this.commonGoalCard).get(1));
                while(myPersonalGoalCard==null){
                    this.myPersonalGoalCard = ((PersonalGoalCard) server.getModel(this.matchIndex,GAME_PGC, myIndex));
                }
                gui.setupPGC(myPersonalGoalCard.getNumber());
            }
            if (myIndex == indexOfPIT) {
                gui.showError(START_YOUR_TURN);
                flowGui();
                getModel();
            } else {
                gui.showError(NOT_YOUR_TURN);
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
                        board = (Board) server.getModel(matchIndex, GAME_BOARD, myIndex);
                        gui.update(board, new Message(board, SET_UP_BOARD));
                        this.commonGoalCard = ((ArrayList<CommonGoalCard>) server.getModel(matchIndex, GAME_CGC, myIndex));
                        gui.update(commonGoalCard.get(0),new Message(commonGoalCard.get(0),UPDATE_SCORINGTOKEN_1));
                        this.commonGoalCard = ((ArrayList<CommonGoalCard>) server.getModel(matchIndex, GAME_CGC, myIndex));
                        gui.update(commonGoalCard.get(1),new Message(commonGoalCard.get(1),UPDATE_SCORINGTOKEN_2));
                        errorReceived = server.sendMessage(matchIndex,myIndex,END);
                        if (errorReceived==END) {
                            gui.showError(END);
                            gui.update(null, new Message(END, END));
                        }
                    } catch (SocketException | UnmarshalException ex) {}
                } while (status != Event.OK);
                getModel();
            }
        }

    }

    /**
     * Method used to handle the various choices that an <b>active player</b> can do.<br>
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
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
        System.out.println(errorReceived.getTUIMsg());
        if (errorReceived == GAME_OVER) {
            Player winner = (Player) server.getModel(matchIndex,GET_WINNER,myIndex);
            uView.gameOver(listOfPlayers);
            if(viewChosen == 2){
                errorReceived = server.sendMessage(matchIndex,myIndex,END);
                if (errorReceived==END) {
                    gui.showError(END);
                    gui.update(null, new Message(END, END));
                    System.exit(0);
                }
            }
        }
        getModel();
    }

    /**
     * Method used to handle the various choices that a <b>passive player</b> can do.<br>
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
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


    /**
     * Method used to ask a player the coordinates of the tiles it wants to pick from the board.<br>
     * @throws IOException if an error occurs*/
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

    /**
     * Method used to ask the player how many tiles it wants to pick from the board.*/
    private void numOfChosenTiles() {
        this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
    }

    /**
     * Method that represents the menu of an active player.<br>
     * @throws IOException if an error occurs*/
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
                case 7 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex);
                    uView.showPlayers(listOfPlayers);
                }
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
    }

    /**
     * Method that returns an event for the call of the method {@code numOfChosenTiles()}.<br>
     * @return an {@link Event} that is the event returned by the server
     * @throws IOException if an error occurs*/
    private Event activeAskNumOfTiles() throws IOException {
        numOfChosenTiles();
        errorReceived = server.sendMessage(this.matchIndex,numberOfChosenTiles, TURN_AMOUNT);
        System.out.println(errorReceived.getTUIMsg());
        return errorReceived;
    }

    /**
     * Method that returns an event for the call of the method {@code chooseTiles()}.<br>
     * @return an {@link Event} that is the event returned by the server
     * @throws IOException if an error occurs*/
    private Event activeAskTiles() throws IOException {
        chooseTiles();
        errorReceived = server.sendMessage(this.matchIndex,cords, TURN_PICKED_TILES);
        System.out.println(errorReceived.getTUIMsg());
        return errorReceived;
    }

    /**
     * Method that returns an event when the player gets asked the column in which it wants to put its tiles.<br>
     * @param tilesInHand is an {@code ArrayList} that represents the tiles that a player ahs previously picked from th board
     * @return an {@link Event} that is the event returned by the server
     * @throws IOException if an error occurs*/
    private Event activeAskColumn(ArrayList<Tile> tilesInHand) throws IOException {
        int column = uView.askColumn();
        errorReceived = server.sendMessage(this.matchIndex, column, TURN_COLUMN);
        System.out.println(errorReceived.getTUIMsg());
        if (errorReceived == Event.OK)
            activePlaceTile(tilesInHand);
        return errorReceived;
    }

    /**
     * Method used to place the tiles in hand of a player into the Bookshelf.<br>
     * @param tilesInHand is an {@code ArrayList} that represents the tiles that a player ahs previously picked from th board
     * @throws IOException if an error occurs*/
    private void activePlaceTile(ArrayList<Tile> tilesInHand) throws IOException {
        int pos;
        for (int i = 0; i < numberOfChosenTiles; i++) {
            do {
                pos = uView.askTileToInsert(tilesInHand);
                errorReceived = server.sendMessage(this.matchIndex,pos, TURN_POSITION);
                System.out.println(errorReceived.getTUIMsg());
            } while (errorReceived != Event.OK);
            playerInTurn.setMyBookshelf((Bookshelf) server.getModel(this.matchIndex,UPDATE_BOOKSHELF, myIndex));
            tilesInHand = (ArrayList<Tile>) server.getModel(this.matchIndex,TURN_POSITION, myIndex);
            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
    }

    /**
     * Method that represents the menu for a passive player.
     * @throws IOException if an error occurs*/
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
                    do{
                        commonGoalCard = ((ArrayList<CommonGoalCard>) server.getModel(matchIndex, GAME_CGC, myIndex));
                    } while (commonGoalCard==null);
                    uView.showCGC(commonGoalCard);
                }
                case 3 -> {
                    System.out.println("Here's your PersonalGoalCard (Shhh don't tell anyone!)");
                    do{
                        myPersonalGoalCard = (PersonalGoalCard) server.getModel(matchIndex, GAME_PGC, myIndex);
                    } while(myPersonalGoalCard==null);
                    uView.showPGC(myPersonalGoalCard);
                }
                case 4 -> {
                    System.out.println("Here's everyone's Bookshelf");
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex); //used to update the bookshelves
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 6 -> {
                    this.listOfPlayers = (ArrayList<Player>) server.getModel(this.matchIndex,GAME_PLAYERS, myIndex); //get the latest update
                    uView.showPlayers(listOfPlayers);
                }
            }
        }
    }


    /**
     * Method used to handle the game flow of the Graphical User Interface.<br>
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
    public void flowGui() throws IOException, InterruptedException {
        int tilesToPick;
        gui.loadPlayers(listOfPlayers);
        out.println("Score del primo:" +listOfPlayers.get(0).getScore());
        do {
            tilesToPick = gui.askTiles(); //ask number of tiles
            errorReceived = server.sendMessage(this.matchIndex,tilesToPick, TURN_AMOUNT);
            gui.showError(errorReceived);
        } while (errorReceived != Event.OK);

        ArrayList<Cord> tilesCords = new ArrayList<>();
        do {
            gui.getBoardView().setTilesPicked(tilesToPick);
            System.out.println("tiles picked " + gui.getBoardView().getTilesPicked());
            tilesCords = gui.getTilesClient();
            System.out.println("tiles cords " + tilesCords.size());
            errorReceived = server.sendMessage(this.matchIndex, tilesCords, TURN_PICKED_TILES);
            System.out.println(errorReceived.getTUIMsg());
            gui.showError(errorReceived);
            System.out.println("errore: " + errorReceived.getMsg());
        } while (errorReceived != Event.OK);

        ArrayList<Tile> tilesInHand = (ArrayList<Tile>) server.getModel(this.matchIndex,TURN_TILE_IN_HAND, myIndex);

        gui.pickTiles(tilesCords, tilesInHand); //adds the tile to tiles in hand

        do {
            int column = gui.chooseColumn();
            errorReceived = server.sendMessage(this.matchIndex, column, TURN_COLUMN);
            System.out.println("errore colonna: " + errorReceived);
            gui.showError(errorReceived);
        }while(errorReceived!=Event.OK);

        for(int i=0; i<tilesToPick; i++){
            int pos = gui.chooseTile();
            gui.addTile(tilesInHand.get(pos));
            errorReceived = server.sendMessage(this.matchIndex, pos, TURN_POSITION);
        }
        gui.endInsertion();
        gui.getHandView().setTileToInsert(-1);

        errorReceived = server.sendMessage(this.matchIndex,null, CHECK_REFILL);
        if (errorReceived == Event.REFILL) {
            this.board = (Board) server.getModel(matchIndex,GAME_BOARD,myIndex);
            gui.update(board,new Message(board,SET_UP_BOARD));
            gui.showError(errorReceived);
        }
        errorReceived = server.sendMessage(this.matchIndex,myIndex, END_OF_TURN);
        if(errorReceived != OK)
            gui.showError(errorReceived);
        if (errorReceived == GAME_OVER) {
            gui.showError(GAME_OVER);

        }
        listOfPlayers = (ArrayList<Player>) server.getModel(matchIndex,GAME_PLAYERS,myIndex); //Used to update the score after placing my tiles

        this.commonGoalCard = ((ArrayList<CommonGoalCard>) server.getModel(matchIndex, GAME_CGC, myIndex));
        gui.update(commonGoalCard.get(0),new Message(commonGoalCard.get(0),UPDATE_SCORINGTOKEN_1));

        this.commonGoalCard = ((ArrayList<CommonGoalCard>) server.getModel(matchIndex, GAME_CGC, myIndex));
        gui.update(commonGoalCard.get(1),new Message(commonGoalCard.get(1),UPDATE_SCORINGTOKEN_2));

        errorReceived = server.sendMessage(matchIndex,myIndex,END);
        gui.update(null,new Message(listOfPlayers,UPDATED_SCORE));
        gui.loadPlayers(listOfPlayers);
    }

    /**
     * Method used to handle the turns.
     * @throws IOException if an error occurs
     * @throws InterruptedException if a thread gets interrupted*/
    public void waitTurn() throws IOException, InterruptedException {
        //AA
        int pitIndex;
        do {
            if(viewChosen==2) {
                listOfPlayers = (ArrayList<Player>) server.getModel(matchIndex, GAME_PLAYERS, myIndex); //Used to update the score after placing my tiles
                gui.update(null, new Message(listOfPlayers, UPDATED_SCORE));
                gui.loadPlayers(listOfPlayers);
            }
            pitIndex = (int) server.getModel(this.matchIndex,GAME_PIT, myIndex);
        } while (pitIndex != myIndex);
        if(viewChosen==1) {
            System.out.println("You turn");
            System.out.println("Press enter to start your turn....");
        }
        else
            gui.showError(START_YOUR_TURN);
        disabledInput = true;
    }

    /**
     * Method used to handle the disconnections.<br>
     * @return a {@link Runnable}*/
    public Runnable controlDisconnection() {
        Thread controlDisconnectionThread = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if(server.getDisconnection(matchIndex)) {
                        err.println("\n\nSomebody disconnected!");
                        Thread.currentThread().interrupt();
                        System.exit(-1);
                    }
                } catch (RemoteException e) {
                    System.err.println("\n\nServer crashed!");
                    Thread.currentThread().interrupt();
                    System.exit(-1);
                }
            }
        });
        controlDisconnectionThread.start();
        return null;
    }

    /**
     * Method used to handle disconnections.
     *@throws RemoteException if an error occurs during a remote call*/
    public void ping() throws RemoteException{
    }

}
