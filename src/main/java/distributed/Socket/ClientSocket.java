package distributed.Socket;

import distributed.messages.SocketMessage;
import model.*;
import util.Cord;
import util.Event;
import view.UserView;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static util.Event.*;
import static util.Event.GAME_PIT;

public class ClientSocket {
    private String address;
    private int port;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;
    private Object serverObj;
    private int myIndex;
    private int myMatch;
    private UserView uView;
    private int viewChosen;
    private String nickname;
    private boolean hasGameStarted = false;
    private Board board;
    private ArrayList<Player> listOfPlayers;
    private ArrayList<CommonGoalCard> commonGoalCard;
    private PersonalGoalCard myPersonalGoalCard;
    private int indexOfPIT;
    private Player playerInTurn;
    private Thread threadWaitTurn;
    private Object lock;
    private boolean disabledInput;
    private boolean isFirstTurn = true;
    private int numberOfChosenTiles;
    private ArrayList<Cord> cords = new ArrayList<>();
    private int column;


    public ClientSocket(String address, int port) throws IOException {
        this.port = port;
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), 1000);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.myIndex = -1;
        this.myMatch = -1;
    }


    public void lobby(UserView userView) throws IOException, ClassNotFoundException {
        uView = userView;
        this.lock = new Object();

        System.out.println("istanzio uView!!");
        Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  try{
                      System.out.println("attendo messaggio...");
                      SocketMessage message = receivedMessageC();
                      System.out.println("ho ricevuto " + message.getMessageEvent());
                      update(message);
                  } catch (IOException | ClassNotFoundException | InterruptedException e) {
                      throw new RuntimeException(e);
                  }
                  //notifyObservers(message);
              }
              System.out.println("esco");
          });
      });

      clientThread.start();
    }

    public void update(SocketMessage message) throws IOException, ClassNotFoundException, InterruptedException {
        switch (message.getMessageEvent()){
            case SET_CLIENT_INDEX ->{
                this.myIndex = message.getClientIndex();
                this.myMatch = message.getMatchIndex();
                update(new SocketMessage(myIndex, myMatch, null, Event.CHOOSE_VIEW));
            }
            case CHOOSE_VIEW -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.viewChosen = uView.userInterface();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.viewChosen, Event.CHOOSE_VIEW));
            }
            case SET_NICKNAME -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.nickname = uView.askPlayerNickname();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.nickname, Event.SET_NICKNAME));
            }
            case ALL_CONNECTED -> {
                startThread();
                getModel();
            }
        }
    }

    public void sendMessageC(SocketMessage mess) throws IOException {
        try{
            //invio messaggio:
            outputStream.writeObject(mess);
            outputStream.flush();
            outputStream.reset();
        }catch (IOException e){
            //TODO disconnessione
            //notifyObserver con messaggio di errore
        }
    }

    public SocketMessage receivedMessageC() throws IOException, ClassNotFoundException {
        SocketMessage message = (SocketMessage) inputStream.readObject();
        return message;
    }

    public void startThread(){
        this.threadWaitTurn = new Thread(() -> {
            synchronized (lock) {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        waitTurn();
                        lock.wait();
                    } catch (InterruptedException | IOException | ClassNotFoundException e) {
                        //throw new RuntimeException(e);
                    }
                }

            }
        }, "WaitForTurnThread");
    }

    public void waitTurn() throws IOException, ClassNotFoundException {
        int pitIndex;
        do {
            sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PIT));
            pitIndex = (int) receivedMessageC().getObj();
        } while (pitIndex != myIndex);
        if(viewChosen==1) {
            System.out.println("You turn");
            System.out.println("Press enter to start your turn....");
        }
        disabledInput = true;
    }

    public void getModel() throws IOException, ClassNotFoundException, InterruptedException {
        disabledInput = false;
        if(!hasGameStarted){
            sendMessageC(new SocketMessage(this.myIndex, this.myMatch,null, GAME_STARTED));
            if(receivedMessageC().getMessageEvent()==Event.OK){
                System.out.println("ricevuto ok");
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
                this.board = (Board) receivedMessageC().getObj();
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_CGC));
                this.commonGoalCard = (ArrayList<CommonGoalCard>) receivedMessageC().getObj();
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PGC));
                this.myPersonalGoalCard = (PersonalGoalCard) receivedMessageC().getObj();
                sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PIT));
                this.indexOfPIT = (int) receivedMessageC().getObj();
                this.playerInTurn = listOfPlayers.get(indexOfPIT);
            }

        }
        System.out.println("ho il model ora devo startare la partita!");

        if(viewChosen==1){
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
        } else if(viewChosen==2){
            //TODO GUI
            /*if (this.isFirstTurn) {
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
            */
        }
    }

    private void activePlay() throws IOException, InterruptedException, ClassNotFoundException {
        isFirstTurn = false;
        playerInTurn = listOfPlayers.get(myIndex);
        activePlayerMenu();
        System.out.println("Here's your Bookshelf:");
        uView.showTUIBookshelf(listOfPlayers.get(indexOfPIT).getMyBookshelf());
        uView.showTUIBoard(this.board);

        Event errorReceived;
        //ASK NUM OF TILES
        do {
            errorReceived = activeAskNumOfTiles();
        } while (errorReceived != Event.OK);

        //Asks the client the coordinates
        do {
            errorReceived = activeAskTiles();
        } while (errorReceived != Event.OK);

        ArrayList<Tile> tilesInHand;
        sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_TILE_IN_HAND));
        tilesInHand = (ArrayList<Tile>) (receivedMessageC().getObj());
        uView.printTilesInHand(tilesInHand);
        uView.showTUIBookshelf(listOfPlayers.get(myIndex).getMyBookshelf());

        //Asking in which column the player wishes to place the picked tiles
        do {
            errorReceived = activeAskColumn(tilesInHand);
        } while (errorReceived != Event.OK);


        //Asking the order of insertion
        sendMessageC(new SocketMessage(myIndex, myMatch, null, CHECK_REFILL));
        errorReceived = (Event) receivedMessageC().getObj();

        //The server checks if the board had to be refilled, the client asks the server
        //if it has been done, if true then it receives an update of the board so that it can be printed
        if (errorReceived == Event.REFILL)
            System.out.println("\u001B[35mThe board had to be refilled and is now ready for the next turn...\u001B[0m");

        sendMessageC(new SocketMessage(myIndex, myMatch, null, END_OF_TURN));
        errorReceived = (Event) receivedMessageC().getObj();
        System.out.println(errorReceived.getMsg());
        getModel();
    }


    private void activePlayerMenu() throws IOException, ClassNotFoundException {
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
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                    this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 7 -> {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                    this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
                    uView.showPlayers(listOfPlayers);
                }
            }
            if (choice != 6)
                System.out.println("What else would you like to do?");
        } while (choice != 6);
    }

    private Event activeAskNumOfTiles() throws IOException, ClassNotFoundException {
        this.numberOfChosenTiles = uView.askNumberOfChosenTiles();
        if(this.numberOfChosenTiles!=-1){
            sendMessageC(new SocketMessage(myIndex, myMatch, numberOfChosenTiles, TURN_AMOUNT));
            SocketMessage message = receivedMessageC();
            if(message.getObj()!=null){
                if(message.getObj()!=Event.OK)
                    System.out.println(((Event)message.getObj()).getMsg());
                return (Event)message.getObj();
            } else {
                return Event.OK;
            }
        } else{
            return OUT_OF_BOUNDS;
        }
    }

    private Event activeAskTiles() throws IOException, ClassNotFoundException {
        chooseTiles();
        sendMessageC(new SocketMessage(myIndex, myMatch, cords, TURN_PICKED_TILES));
        SocketMessage message = receivedMessageC();
        if(message.getObj()!=Event.OK)
            System.out.println(((Event)message.getObj()).getMsg());
        return (Event)message.getObj();
    }

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

    private Event activeAskColumn(ArrayList<Tile> tilesInHand) throws IOException, ClassNotFoundException {
        column = uView.askColumn();
        sendMessageC(new SocketMessage(myIndex, myMatch, column, TURN_COLUMN));
        SocketMessage message = receivedMessageC();
        if (message.getObj() == Event.OK) {
            activePlaceTile(tilesInHand);
        }else
            System.out.println(((Event)message.getObj()).getMsg());
        return (Event) message.getObj();
    }

    private void activePlaceTile(ArrayList<Tile> tilesInHand) throws IOException, ClassNotFoundException {
        int pos;
        for (int i = 0; i < numberOfChosenTiles; i++) {
            SocketMessage message;
            do {
                pos = uView.askTileToInsert(tilesInHand);
                sendMessageC(new SocketMessage(myIndex, myMatch, pos, TURN_POSITION));
                message = receivedMessageC();
                System.out.println(((Event)message.getObj()).getMsg());
            } while ((Event)message.getObj() != Event.OK);

            sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, UPDATE_BOOKSHELF));
            Bookshelf bookshelf = (Bookshelf) receivedMessageC().getObj();
            playerInTurn.setMyBookshelf(bookshelf);

            sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, TURN_POSITION));
            tilesInHand = (ArrayList<Tile>) receivedMessageC().getObj();

            uView.printTilesInHand(tilesInHand);
            uView.showTUIBookshelf(playerInTurn.getMyBookshelf());
        }
    }



    private void passivePlay() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("It's not your turn, here are some actions you can do!");
        passivePlayerMenu();
        getModel();
    }

    private void passivePlayerMenu() throws IOException, ClassNotFoundException {
        uView.askPassiveAction();
        int choice = uView.waitInput();
        if (!disabledInput) {
            switch (choice) {
                case 1 -> {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_BOARD));
                    this.board = (Board) receivedMessageC().getObj();
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
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                    this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
                    for (Player player : listOfPlayers) {
                        System.out.println("\u001B[36m" + player.getNickname() + "\u001B[0m's bookshelf:");
                        uView.showTUIBookshelf(player.getMyBookshelf());
                    }
                }
                case 5 -> {
                    uView.chatOptions(listOfPlayers.get(myIndex));
                }
                case 6 -> {
                    sendMessageC(new SocketMessage(myIndex, myMatch, ASK_MODEL, GAME_PLAYERS));
                    this.listOfPlayers = (ArrayList<Player>) receivedMessageC().getObj();
                    uView.showPlayers(listOfPlayers);
                }
            }
        }
    }

    public int getMyIndex(){
        return myIndex;
    }

    public int getMyMatch(){
        return myMatch;
    }


    public void closeConnection(){
        //TODO implement this
    }

    /**
     *
     */
    public void disconnected() {

    }



    public void disconnect(){
        //TODO implement this
    };


    public void setMatchIndex(int i){
        this.myMatch = i;
    }

    public void setMyIndex(int i){
        this.myIndex = i;
    }
}
