package distributed.Socket;

import distributed.messages.Message;
import distributed.messages.SocketMessage;
import model.*;
import org.jetbrains.annotations.NotNull;
import util.Cord;
import util.Event;
import view.GUI.GUIView;
import view.UserView;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static util.Event.*;
import static util.Event.GAME_PIT;

/**Class that represents a Socket client*/
public class ClientSocket {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;
    private int myIndex;
    private int myMatch;
    private UserView uView;
    private int viewChosen;
    private boolean hasGameStarted = false;
    private Board board;
    private ArrayList<Player> listOfPlayers;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private Player playerInTurn;
    private boolean disabledInput;
    private boolean isFirstTurn = true;
    private int numberOfChosenTiles;
    private final ArrayList<Cord> cords = new ArrayList<>();
    private GUIView gui;
    private ArrayList<Cord> tilesCords = new ArrayList<>();
    private ArrayList<Tile> tilesInHand;
    private boolean active = false;
    private int choice=0;
    private int count=0;
    private Thread passiveThread;
    private Object lockPrint;
    private boolean enterHasBeenPressed;
    private boolean disablePassivePrint;
    private boolean refill = false;


    /**
     * Constructor of the Class. This initializes the connection of a client.
     * @param port is an int thar represents the port
     * @param address is a {@code String} that represents the address
     * @throws IOException if an error occurs*/
    public ClientSocket(String address, int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(address, port), 1000);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.myIndex = -1;
        this.myMatch = -1;
    }

    /**
     * Method used as the lobby of a match for a client.
     * @param userView is a {@link UserView}
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found*/
    public void lobby(UserView userView) throws IOException, ClassNotFoundException {
        uView = userView;
        lockPrint = new Object();

        Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  try{
                      SocketMessage message = receivedMessageC();
                      update(message);
                  } catch (IOException | ClassNotFoundException | InterruptedException e) {
                      throw new RuntimeException(e);
                  }
              }
          });
      },"ClientThread");
      clientThread.start();
    }

    /**
     * Method used to create a thread for the passive Player.*/
    public void startThreadPassive() {
        passiveThread = new Thread(()-> {while(!Thread.currentThread().isInterrupted()) {
            try {
                if (choice == 1 || choice == 4 || choice == 5) {
                    while(disablePassivePrint){
                    }
                }
                passivePlay();
            } catch (IOException | InterruptedException | ClassNotFoundException e) {
            }
        }
        },"startThreadPassive");
        passiveThread.start();
    }

    /**
     * Method used to perform a specific update, based on the received message.
     * @param message is a {@link Message} that contains the information used to perform the update
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * @throws InterruptedException if a thread gets interrupted
     * */
    public void update(@NotNull SocketMessage message) throws IOException, ClassNotFoundException, InterruptedException {
        switch (message.getMessageEvent()){
            case SET_CLIENT_INDEX ->{
                this.myIndex = message.getClientIndex();
                this.myMatch = message.getMatchIndex();
                update(new SocketMessage(myIndex, myMatch, null, CHOOSE_VIEW));
            }
            case CHOOSE_VIEW -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getTUIMsg());
                }
                this.viewChosen = uView.userInterface();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.viewChosen, CHOOSE_VIEW));
            }
            case SET_NICKNAME -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getTUIMsg());
                }
                String nickname = uView.askPlayerNickname();
                sendMessageC(new SocketMessage(myIndex, myMatch, nickname, SET_NICKNAME));
            }
            case ALL_CONNECTED -> {
                hasGameStarted = true;
                getModel();
                sendMessageC(new SocketMessage(myIndex, myMatch, null, START_THREAD));
            }
            case START, CHECK_MY_TURN -> {
                sendMessageC(new SocketMessage(myIndex, myMatch, CHECK_MY_TURN, GAME_PIT));
            }
            case START_YOUR_TURN -> {
                System.out.println("It's your turn!");
                active = true;
                if(viewChosen==1){
                    if(!isFirstTurn) {
                        disabledInput = true;
                            passiveThread.interrupt();
                        System.out.println("Press enter to start your turn!");
                        synchronized (lockPrint) {
                            lockPrint.wait();
                        }
                        getModel();
                    }
                    isFirstTurn = false;
                    playerInTurn = listOfPlayers.get(myIndex);
                    activePlayerMenu();
                    System.out.println("Here's your Bookshelf:");
                    uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
                    uView.showTUIBoard(this.board);

                    activeAskNumOfTiles();

                } if(viewChosen==2){
                    if(!isFirstTurn)
                        getModel();
                    isFirstTurn = false;
                    gui.showError(START_YOUR_TURN);
                    askNumOfTileToPickGUI();
                }
            }
            case NOT_YOUR_TURN -> {
                active = false;
                if(viewChosen==1){
                    disabledInput = false;
                    startThreadPassive();
                    isFirstTurn=false;
                } else if(viewChosen==2){
                    gui.showError(NOT_YOUR_TURN);
                }
            }
            case GAME_BOARD -> {
                if(viewChosen==1) {
                    this.board = (Board) message.getObj();
                    if (!active) {
                        System.out.println("Here's the game board...");
                        uView.showTUIBoard(board);
                        disablePassivePrint = false;
                    }

                } else if(viewChosen==2){
                    this.board = (Board) message.getObj();
                    gui.update(board,new Message(board,SET_UP_BOARD));
                    if(this.refill){
                        sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));
                        refill=false;
                    }
                }
            }
            case GAME_PLAYERS -> {
                if(viewChosen==1) {
                    this.listOfPlayers = (ArrayList<Player>) message.getObj();
                    if (!active) {
                        if (choice == 4) {
                            for (Player player : listOfPlayers) {
                                System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                                uView.showTUIBookshelf(player.getMyBookshelf());
                            }
                            disablePassivePrint = false;
                        } else if (choice == 5) {
                            this.listOfPlayers = (ArrayList<Player>) message.getObj();
                            uView.showPlayers(listOfPlayers);
                            disablePassivePrint = false;
                        }
                    } else {
                        this.listOfPlayers = (ArrayList<Player>) message.getObj();
                        uView.showPlayers(listOfPlayers);
                        sendMessageC(new SocketMessage(myIndex, myMatch, END_OF_TURN, END_OF_TURN));
                    }
                } else if(viewChosen==2){
                    System.out.println(message.getObj());
                    listOfPlayers = (ArrayList<Player>) message.getObj(); //Used to update the score after placing my tiles
                    gui.update(null,new Message(listOfPlayers,UPDATED_SCORE));
                    gui.loadPlayers(listOfPlayers);
                    sendMessageC(new SocketMessage(myIndex, myMatch, END_OF_TURN, END_OF_TURN));
                }
            }
            case TURN_AMOUNT -> {
                if(viewChosen==1){
                    if(message.getObj()!=null) {
                        if (message.getObj() != OK) {
                            System.out.println(((Event) message.getObj()).getTUIMsg());
                            activeAskNumOfTiles();
                        } else {
                            activeAskTiles();
                        }
                    }
                } else if(viewChosen==2){
                    gui.showError((Event) message.getObj());
                    if(message.getObj()!=Event.OK){
                        askNumOfTileToPickGUI();
                    } else{
                        setTilesPickedGUI();
                    }
                }

            }
            case TURN_PICKED_TILES -> {
                if(viewChosen==1){
                    if(message.getObj()!= OK) {
                        System.out.println(((Event) message.getObj()).getTUIMsg());
                        activeAskTiles();
                    } else {
                        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_TILE_IN_HAND));
                    }
                } else if(viewChosen==2){
                    System.out.println(((Event)message.getObj()).getTUIMsg());
                    gui.showError(((Event)message.getObj()));
                    if(message.getObj() !=OK){
                        setTilesPickedGUI();
                    } else{
                        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_TILE_IN_HAND));
                    }
                }

            }
            case TURN_TILE_IN_HAND -> {
                tilesInHand = (ArrayList<Tile>) message.getObj();
                if(viewChosen==1){
                    System.out.println(tilesInHand);
                    uView.printTilesInHand(tilesInHand);
                    uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());
                    activeAskColumn();
                } else if (viewChosen==2){
                    gui.pickTiles(tilesCords, tilesInHand); //adds the tile to tiles in hand
                    askColumnGUI();
                }
            }
            case TURN_COLUMN -> {
                if(viewChosen==1){
                    if (message.getObj() == OK) {
                        activePlaceTile(tilesInHand);
                    }else {
                        System.out.println(((Event) message.getObj()).getTUIMsg());
                        activeAskColumn();

                    }
                } else if(viewChosen==2){
                    gui.showError((Event) message.getObj());
                    if(message.getObj()!=OK){
                        askColumnGUI();
                    } else {
                        addTilesInHandGUI();
                    }
                }
            }
            case TURN_POSITION -> {
                if(viewChosen==1){
                    if(message.getObj()!=OK){
                        System.out.println(((Event)message.getObj()).getTUIMsg());
                        activePlaceTile(tilesInHand);
                    } else if(count<numberOfChosenTiles){
                        count++;
                        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, UPDATE_BOOKSHELF));
                    }
                }
            } case UPDATE_BOOKSHELF -> {
                if(viewChosen==1){
                    if(count<=numberOfChosenTiles){
                        Bookshelf bookshelf = (Bookshelf) message.getObj();
                        playerInTurn.setMyBookshelf(bookshelf);
                        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GET_TILES_HAND));
                    }
                }
            }
            case GET_TILES_HAND -> {
                if(viewChosen==1){
                    uView.printTilesInHand((ArrayList<Tile>) message.getObj());
                    uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
                    if(count<numberOfChosenTiles) {
                        activePlaceTile((ArrayList<Tile>) message.getObj());
                    } else {
                        count = 0;
                        sendMessageC(new SocketMessage(myIndex, myMatch, null, CHECK_REFILL));
                    }
                }
            }
            case CHECK_REFILL -> {
                if (viewChosen == 1) {
                    if (message.getObj() == REFILL)
                        System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");
                    sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));
                } else if(viewChosen==2){
                    if (message.getObj() == Event.REFILL) {
                        this.refill=true;
                        System.out.println("REFILL!!!" + refill); //FIXME
                        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
                    } else{
                        sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));
                    }
                }
            }
            case END_OF_TURN -> {
                if(viewChosen==1){
                    System.out.println(((Event)(message.getObj())).getTUIMsg());
                    if (message.getObj() == GAME_OVER) {
                        uView.gameOver(listOfPlayers);
                        System.exit(0);
                    }
                    sendMessageC(new SocketMessage(myIndex, myMatch, END_OF_TURN, END_OF_TURN));
                } else if(viewChosen==2){
                    System.out.println(message.getObj());
                    if(message.getObj() != OK) {
                        gui.showError(((Event) message.getObj()));
                    }
                    if (message.getObj() == GAME_OVER) {
                        gui.showError(END);
                        do {
                            // Do Until GUI closed
                        }while (true);
                        //System.exit(10);
                    }
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                }
            }
            case UPDATED_GAME_BOARD -> {
                if(viewChosen==2) {
                    this.board = (Board) message.getObj();
                    gui.update(board,new Message(board,SET_UP_BOARD));
                }
            }
            case DISCONNECTED -> {
                System.out.println("Disconnection!\n");
                System.exit(-1);
            }
            case UPDATE_SCORINGTOKEN_1 -> {
                if(viewChosen==2) { //TODO: NON C'ENTRA CON QUESTA RIGA MA TOGLIERE DAL "PASSIVE" (ANCHE SE NON ESISTE PER LA GUI) IL System.exit IN MODO CHE LEGGA IL MESSAGGIO DI GAME OVER
                    commonGoalCard.set(0, (CommonGoalCard) message.getObj());
                    gui.update(commonGoalCard.get(0), new Message(commonGoalCard.get(0),UPDATE_SCORINGTOKEN_1));
                }
            }
            case UPDATE_SCORINGTOKEN_2 ->{
                if(viewChosen==2) {
                    commonGoalCard.set(1,(CommonGoalCard) message.getObj());
                    gui.update(commonGoalCard.get(1), new Message(commonGoalCard.get(1),UPDATE_SCORINGTOKEN_2));
                }
            }
            case UPDATED_SCORE -> {
                if(viewChosen==2){
                    gui.update(null, new Message(message.getObj(), UPDATED_SCORE));
                }
            }
            case END -> {
                if (viewChosen == 1) {
                    uView.gameOver(listOfPlayers);
                    System.exit(0);
                }
                else {
                    gui.showError(END);
                    gui.update(null, new Message(END, END));
                    passiveThread.interrupt();
                }
            }
        }
    }

    /**
     * Method used to send a socket Message.
     * @param mess is a {@link SocketMessage} that has to be sent
     * @throws IOException if an error occurs*/
    public void sendMessageC(SocketMessage mess) throws IOException {
        try{
            outputStream.writeObject(mess);
            outputStream.flush();
            outputStream.reset();
        }catch (IOException e){
            System.out.println("Server crashed!\n");
            System.exit(-1);
        }
    }

    /**
     * Method used to receive a socket Message.
     * @return a {@link SocketMessage}
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * */
    public SocketMessage receivedMessageC() throws IOException, ClassNotFoundException {
        return (SocketMessage) inputStream.readObject();
    }

    /**
     * Method used to get information from the model.
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     */
    public void getModel() throws IOException, ClassNotFoundException {
        if (!hasGameStarted) {
            sendMessageC(new SocketMessage(this.myIndex, this.myMatch, null, GAME_STARTED));
            while (receivedMessageC().getObj() != Event.OK) {
                sendMessageC(new SocketMessage(this.myIndex, this.myMatch, null, GAME_STARTED));
            }
            hasGameStarted = true;
        }
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
        this.board = (Board) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
        this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_CGC));
        this.commonGoalCard = (ArrayList<CommonGoalCard>) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PGC));
        PersonalGoalCard myPersonalGoalCard = (PersonalGoalCard) receivedMessageC().getObj();
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PIT));
        int index = (int) receivedMessageC().getObj();
        if(index != myIndex)
            sendMessageC(new SocketMessage(myIndex, myMatch, null, START_THREAD));
        if (viewChosen == 2) {
            if (this.isFirstTurn) {
                gui = new GUIView();
                sendMessageC(new SocketMessage(myIndex, myMatch, gui, ADD_OBSERVER));
                this.isFirstTurn = false;
                gui.update(null, new Message(listOfPlayers, UPDATED_SCORE));
                gui.updateBoard(this.board);
                gui.setupCGC(commonGoalCard.get(0));
                gui.setupCGC(commonGoalCard.get(1));
                gui.setupPGC(myPersonalGoalCard.getNumber());
            }
        }
    }


    /**Method used to create an active player menu.
     * @throws IOException if an error occurs
     */
    private void activePlayerMenu() throws IOException {
        System.out.println("Would you like to do any of these actions before making your move?");
        do{
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
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> uView.showPlayers(listOfPlayers);
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
    }

    /**
     * Method used to ask a player the number of tiles it wants to pick.
     * @throws IOException if an error occurs
     */
    private void activeAskNumOfTiles() throws IOException {
        do{
            this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
            if(this.numberOfChosenTiles!=-1){
                sendMessageC(new SocketMessage(myIndex, myMatch, numberOfChosenTiles, TURN_AMOUNT));
            } else{
                System.out.println(Event.OUT_OF_BOUNDS.getTUIMsg());
            }
        } while(this.numberOfChosenTiles==-1);
    }

    /**
     * Method used to ask a player the tiles it wants to pick. This sends a message.
     * @throws IOException if an error occurs
     */
    private void activeAskTiles() throws IOException {
        chooseTiles();
        sendMessageC(new SocketMessage(myIndex, myMatch, cords, TURN_PICKED_TILES));
    }


    /**
     * Method used to make a player pick the tiles from the board.
     * @throws IOException if an error occurs*/
    public void chooseTiles() throws IOException {
        cords.removeAll(cords);
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
     * Method used to ask a player te column.
     * @throws IOException if an error occurs
     */
    private void activeAskColumn() throws IOException {
        int column = uView.askColumn();
        sendMessageC(new SocketMessage(myIndex, myMatch, column, TURN_COLUMN));
    }

    /**
     * Method used to ask a player the order of the tiles.
     * @throws IOException if an error occurs
     */
    private void activePlaceTile(ArrayList<Tile> tilesInHand) throws IOException {
        int pos = uView.askTileToInsert(tilesInHand);
        sendMessageC(new SocketMessage(myIndex, myMatch, pos, TURN_POSITION));
    }

    /**
     * Method used to define the actions for a passive player.
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found
     * @throws InterruptedException if a thread gets interrupted */
    private void passivePlay() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("It's not your turn, here are some actions you can do!");
        active=false;
        enterHasBeenPressed = false;
        passivePlayerMenu();
    }

    /**
     * Method that represents the passive player menu.
     * @throws IOException if an error occurs
     */
    private void passivePlayerMenu() throws IOException {
        uView.askPassiveAction();
        choice = uView.waitInput();
        if(disabledInput) {
            enterHasBeenPressed = true;
            synchronized (lockPrint) {
                lockPrint.notifyAll();
            }
            return;
        }
        if (!disablePassivePrint) {
            switch (choice) {
                case 1 -> {
                    disablePassivePrint = true;
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
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
                    disablePassivePrint = true;
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                }
                case 5 -> {
                    disablePassivePrint = true;
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                }
            }
        }
    }

    /**
     * Method used to ask a player how many tiles it wants to pick (for GUI).
     * @throws IOException if an error occurs*/
    public void askNumOfTileToPickGUI () throws IOException {
        numberOfChosenTiles = gui.askTiles(); //ask number of tiles
        sendMessageC(new SocketMessage(myIndex, myMatch, numberOfChosenTiles, TURN_AMOUNT));
    }

    /**
     * Method used to set the number of chosen tiles (for GUI).
     * @throws IOException if an error occurs*/
    public void setTilesPickedGUI() throws IOException {
        gui.getBoardView().setTilesPicked(numberOfChosenTiles);
        tilesCords = gui.getTilesClient();
        sendMessageC(new SocketMessage(myIndex, myMatch, tilesCords, TURN_PICKED_TILES));
    }

    /**
     * Method used to ask the number of a column (for GUI).
     * @throws IOException if an error occurs*/
    public void askColumnGUI() throws IOException {
        int column = gui.chooseColumn();
        sendMessageC(new SocketMessage(myIndex, myMatch, column, TURN_COLUMN));
    }

    /**
     * Method used to add the picked tiles into the hand (for GUI).
     * @throws IOException if an error occurs*/
    public void addTilesInHandGUI() throws IOException {
        for(int i=0; i<numberOfChosenTiles; i++) {
            int pos = gui.chooseTile();
            gui.addTile(tilesInHand.get(pos));
            sendMessageC(new SocketMessage(myIndex, myMatch, pos, TURN_POSITION));
        }
        gui.endInsertion();
        gui.getHandView().setTileToInsert(-1);
        sendMessageC(new SocketMessage(myIndex, myMatch, null, CHECK_REFILL));

    }

    /**Method used to get the index.
     * @return an int that represents the index*/
    public int getMyIndex(){
        return myIndex;
    }

    /**Method used to get the match.
     * @return an int that represents the match*/
    public int getMyMatch(){
        return myMatch;
    }
}
